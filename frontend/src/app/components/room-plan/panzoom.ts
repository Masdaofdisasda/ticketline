export const panzoom = (element: HTMLElement, options = new Options(), listenerElement?: HTMLElement) => {

    // Default Parameters
    const pan = options.pan !== false;
    const zoom = options.zoom !== false;		// Default: true
    const bound = (['inner', 'outer', 'none'].includes(options.bound)) ? options.bound : 'inner';
    const wheelStep = (options.wheelStep > 0.01 && options.wheelStep < 4) ? options.wheelStep : 0.2;
    const scaleMin = (options.scaleMin > 0.01 && options.scaleMin < 20) ? options.scaleMin : 0.01;
    const scaleMax = (options.scaleMax > 0.01 && options.scaleMax < 20)
        ? ( (options.scaleMax > scaleMin) ? options.scaleMax : scaleMin ) : 10;

    // For panning (translate)
    let lastPosX: number;
    let lastPosY: number;					// Needed because of decimals
    let posXmin: number;
    let posYmin: number;
    let posXmax: number;
    let posYmax: number;
    let parentScale; 						// Needed for avoid calculate every pointermove
    let vvpScale; let dprScale;			// Needed to take into account e.movementX in touch screens

    // For touching devies
    let lastTouchX; let lastTouchY;		// To calculate delta position when moving fingers
    let isPinching = false;
    let isTouching = false;
    let pinchDist1;

    const makeZoomable = (elem: HTMLElement) => {
      const isValid = normalize(elem);
      if (!isValid) {
        return;
      }

      if (zoom) {
        elem.addEventListener('wheel', handleWheel);
        // elem.onwheel = handleWheel;
      }
      if (pan) {
        // Touch events, needed for pinch/zoom
        elem.addEventListener('touchstart', handleTouchstart);
        // elem.ontouchstart = handleTouchstart;
        elem.addEventListener('touchmove', handleTouchmove);
        // elem.ontouchmove = handleTouchmove;
        elem.addEventListener('touchend', handleTouchend);
        // elem.ontouchend = handleTouchend;

        // Pointer events, needed for move
        elem.addEventListener('pointerdown', handlePointerdown);
        // elem.onpointerdown = handlePointerdown;
        elem.addEventListener('pointermove', handlePointermove);
        // elem.onpointermove = handlePointermove;
        elem.addEventListener('pointerup', handlePointerup);
        // elem.onpointerup = handlePointerup;
        // elem.addEventListener("pointercancel", handle_pointerup);
        // elem.addEventListener("pointerout", handle_pointerup);
        // elem.addEventListener("pointerleave", handle_pointerup);

        // elem.addEventListener("gotpointercapture", handle_gotpointercapture);	// Not needed for now
      }
    };

    const normalize = (elem) => {
      const width = elem.offsetWidth;
      const widthp = elem.parentNode.offsetWidth;
      const height = elem.offsetHeight;
      const heightp = elem.parentNode.offsetHeight;

      if (width > widthp) {
        if (bound === 'inner' && (width > widthp || height > heightp)) {
          console.error('panzoom() error: In the \'inner\' mode, with or height must be smaller than its container (parent)');
          return false;
        } else if (bound === 'outer' && (width < widthp || height < heightp)) {
          console.error('panzoom() error: In the \'outer\' mode, with or height must be larger than its container (parent)');
          return false;
        }
      }

      elem.style.position = 'absolute';
      elem.style.removeProperty('margin');
      elem.style.backgroundSize = 'cover';
      return true;
    };

    const doMove = (elem: HTMLElement, deltaX, deltaY) => {
      lastPosX += deltaX;		// Needed because of decimals
      lastPosY += deltaY;		// Needed because of decimals

      if (bound !== 'none') {
        lastPosX = Math.min(Math.max(posXmin, lastPosX), posXmax);	// Restrict Pos X
        lastPosY = Math.min(Math.max(posYmin, lastPosY), posYmax);	// Restrict Pos Y
      }

      elem.style.position = 'absolute';
      elem.style.left = (lastPosX + 'px');
      elem.style.top = (lastPosY + 'px');
    };

    const doZoom = (elem, deltaScale, offsetX, offsetY) => {
      const matrix = new WebKitCSSMatrix(getComputedStyle(elem).getPropertyValue('transform'));
      const {a: scaleX, b: skewY, c: skewX, d: scaleY, e: translateX, f: translateY} = matrix;
      const {x, y, width, height} = elem.getBoundingClientRect();
      const {x: xp, y: yp, width: widthp, height: heightp} = elem.parentNode.getBoundingClientRect();

      deltaScale *= scaleX;	// Smooth deltaScale

      let newScale = scaleX + deltaScale;		// let newScale = scaleX + deltaScale/vvpScale/dprScale;

      let posX;
      let posY;
      let maxScaleX; let maxScaleY; let maxScale;
      let minScaleX; let minScaleY; let minScale;
      if (bound === 'inner') {
        maxScaleX = widthp / elem.offsetWidth;
        maxScaleY = heightp / elem.offsetHeight;
        maxScale = Math.min(maxScaleX, maxScaleY, scaleMax);
        if (newScale > maxScale) {
          deltaScale = 0;
        }
        newScale = Math.min(Math.max(scaleMin, newScale), maxScale);
      } else if (bound === 'outer') {
        minScaleX = widthp / elem.offsetWidth;
        minScaleY = heightp / elem.offsetHeight;
        minScale = Math.max(minScaleX, minScaleY, scaleMin);
        if (newScale < minScale || newScale > scaleMax) {
            return;
        }
      } else if (bound === 'none') {
        if (newScale < scaleMin || newScale > scaleMax) {
            deltaScale = 0;
        }
        newScale = Math.min(Math.max(scaleMin, newScale), scaleMax);
      }

      posX = elem.offsetLeft + deltaScale * (elem.offsetWidth / 2 - offsetX);
      posY = elem.offsetTop + deltaScale * (elem.offsetHeight / 2 - offsetY);

      // Set Position Bounds
      // let posXmin; let posYmin; let posXmax; let posYmax;
      if (bound === 'inner') {
        posXmin = elem.offsetWidth / 2 * (newScale - 1) - translateX;
        posYmin = elem.offsetHeight / 2 * (newScale - 1) - translateY;
        posXmax = elem.parentNode.offsetWidth - elem.offsetWidth - elem.offsetWidth / 2 * (newScale - 1) - translateX;
        posYmax = elem.parentNode.offsetHeight - elem.offsetHeight - elem.offsetHeight / 2 * (newScale - 1) - translateY;
        posX = Math.min(Math.max(posXmin, posX), posXmax);		// Restrict
        posY = Math.min(Math.max(posYmin, posY), posYmax);		// Restrict

      } else if (bound === 'outer') {
        posXmax = elem.offsetWidth / 2 * (newScale - 1) - translateX;
        posYmax = elem.offsetHeight / 2 * (newScale - 1) - translateY;
        posXmin = elem.parentNode.offsetWidth - elem.offsetWidth - elem.offsetWidth / 2 * (newScale - 1) - translateX;
        posYmin = elem.parentNode.offsetHeight - elem.offsetHeight - elem.offsetHeight / 2 * (newScale - 1) - translateY;
        posX = Math.min(Math.max(posXmin, posX), posXmax);		// Restrict
        posY = Math.min(Math.max(posYmin, posY), posYmax);		// Restrict
      } else if (bound === 'none') {

      }

      elem.style.transform = `matrix(${newScale}, ${skewY}, ${skewX}, ${newScale}, ${translateX}, ${translateY})`;

      elem.style.left =  posX + 'px';
      elem.style.top =  posY + 'px';
    };

    const handlePointerdown = (e) => {
      if (options.panCondition ? !options.panCondition() : false) {
        return;
      }

      e.preventDefault();
      e.stopPropagation();

      vvpScale = window.visualViewport.scale;		// It's pinch default gesture zoom (Android). Ignore in Desktop
      dprScale = window.devicePixelRatio;			// Needed if e.screenX is used. Ignore in Mobile

      // Set Last Element Position. Needed because event offset doesn't have decimals. And decimals will be needed when dragging
      lastPosX = element.offsetLeft || 0;
      lastPosY = element.offsetTop || 0;

      // Set Position Bounds
      const matrix = new WebKitCSSMatrix(getComputedStyle(e.target).getPropertyValue('transform'));
      const {a: scaleX, b: skewY, c: skewX, d: scaleY, e: translateX, f: translateY} = matrix;
      const scale = scaleX;

      // Set Position Bounds
      if (bound === 'inner') {
        posXmin = e.target.offsetWidth / 2 * (scale - 1) - translateX;
        posYmin = e.target.offsetHeight / 2 * (scale - 1) - translateY;
        posXmax = e.target.parentNode.offsetWidth - e.target.offsetWidth - e.target.offsetWidth / 2 * (scale - 1) - translateX;
        posYmax = e.target.parentNode.offsetHeight - e.target.offsetHeight - e.target.offsetHeight / 2 * (scale - 1) - translateY;
      } else if (bound === 'outer') {
        posXmax = e.target.offsetWidth / 2 * (scale - 1) - translateX;
        posYmax = e.target.offsetHeight / 2 * (scale - 1) - translateY;
        posXmin = e.target.parentNode.offsetWidth - e.target.offsetWidth - e.target.offsetWidth / 2 * (scale - 1) - translateX;
        posYmin = e.target.parentNode.offsetHeight - e.target.offsetHeight - e.target.offsetHeight / 2 * (scale - 1) - translateY;
      }

      const {x: px1, y: py1, width: pwidth1, height: pheight1} = (element.parentNode as HTMLElement).getBoundingClientRect();
      const pwidth2 = (element.parentNode as HTMLElement).offsetWidth;
      parentScale = pwidth1 / pwidth2;

      element.setPointerCapture(e.pointerId);	// https://developer.mozilla.org/en-US/docs/Web/API/Pointer_Lock_API
    };

    const handlePointermove = (e) => {
      if (options.panCondition ? !options.panCondition() : false) {
        return;
      }
      if (isTouching) {
        return;
      }
      if (!element.hasPointerCapture(e.pointerId)) {
         return;
      }
      e.preventDefault();
      e.stopPropagation();

      const deltaX = e.movementX / parentScale / dprScale;		// vvpScale It's pinch default gesture zoom (Android). Ignore in Desktop
      const deltaY = e.movementY / parentScale / dprScale;		// vvpScale It's pinch default gesture zoom (Android). Ignore in Desktop

      doMove(element, deltaX, deltaY);
    };

    const handlePointerup = (e) => {
      e.preventDefault();
      e.stopPropagation();
      e.target.releasePointerCapture(e.pointerId);
    };

    const handleTouchstart = (e) => {
      e.preventDefault();
      e.stopPropagation();
      isTouching = true;

      // Check if two fingers touched screen. If so, handle Zoom
      if (e.targetTouches.length === 2) {
        isPinching = true;
        pinchDist1 = Math.hypot( // get rough estimate of distance between two fingers
          e.touches[0].pageX - e.touches[1].pageX,
          e.touches[0].pageY - e.touches[1].pageY
        );
      }

      lastTouchX = e.touches[0].pageX;
      lastTouchY = e.touches[0].pageY;

      // It continues handling pointerdown event
    };

    const handleTouchmove = (e) => {
      // Check if two fingers touched screen. If so, handle Zoom
      if (e.targetTouches.length === 2 && e.changedTouches.length === 2) {
        const distX = e.touches[0].pageX - e.touches[1].pageX;
        const distY = e.touches[0].pageY - e.touches[1].pageY;
        const pinchDist2 = Math.hypot(	distX, distY); // get rough estimation of new distance between fingers
        const deltaScale = (pinchDist2 - pinchDist1) / 50;
        pinchDist1 = pinchDist2;

        const {x, y, width, height} = e.target.getBoundingClientRect();

        const offsetX0 = (e.touches[0].clientX - x) / width * e.target.offsetWidth;
        const offsetY0 = (e.touches[0].clientY - y) / height * e.target.offsetHeight;
        const offsetX1 = (e.touches[1].clientX - x) / width * e.target.offsetWidth;
        const offsetY1 = (e.touches[1].clientY - y) / height * e.target.offsetHeight;
        const offsetX = offsetX0 + (offsetX1 - offsetX0) / 2;
        const offsetY = offsetY0 + (offsetY1 - offsetY0) / 2;

        doZoom(e.target, deltaScale, offsetX, offsetY);
      } else if (e.targetTouches.length === 1 && !isPinching) {
        // vvpScale It's pinch default gesture zoom (Android). Ignore in Desktop
        const deltaX = (e.touches[0].pageX - lastTouchX) / parentScale;
        const deltaY = (e.touches[0].pageY - lastTouchY) / parentScale;		//
        lastTouchX = e.touches[0].pageX;
        lastTouchY = e.touches[0].pageY;
        // Else, it is a drag. Handle with pointermouse event

        doMove(element, deltaX, deltaY);
      }
    };

    const handleTouchend = (e) => {
      if (e.targetTouches.length === 0) {
        isTouching = false;
        isPinching = false;
      }
    };

    const handleWheel = (e) => {
      e.preventDefault();
      e.stopPropagation();

      const deltaScale =  e.wheelDelta * wheelStep / 120;
      doZoom(element, deltaScale, e.offsetX, e.offsetY);
    };

    makeZoomable(listenerElement || element);

    // this is necessary because otherwise the zooming according to the mouse does not work
    doMove(element, 0, 0);

    return true;
  };

  export class Options {
    pan?: boolean;
    zoom?: boolean;
    bound?: 'none'|'inner'|'outer';
    wheelStep?: number;
    scaleMin?: number;
    scaleMax?: number;
    initialScale?: number;
    panCondition ? =  () => true;
    zoomCondition ? = () => true;
  }
