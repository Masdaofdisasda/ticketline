import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {

  constructor() {
  }


  /**
   * Converts an RGB color value to HSL. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
   * Assumes r, g, and b are contained in the set [0, 255] and
   * returns h, s, and l in the set [0, 1].
   *
   * @param  r       The red color value
   * @param  g       The green color value
   * @param  b       The blue color value
   * @return  Array           The HSL representation
   */
  public rgbToHsl(r: number, g: number, b: number) {
    r /= 255;
    g /= 255;
    b /= 255;

    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    let h;
    let s;
    const l = (max + min) / 2;

    if (max === min) {
      h = s = 0; // achromatic
    } else {
      const d = max - min;
      s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

      switch (max) {
        case r:
          h = (g - b) / d + (g < b ? 6 : 0);
          break;
        case g:
          h = (b - r) / d + 2;
          break;
        case b:
          h = (r - g) / d + 4;
          break;
      }

      h /= 6;
    }

    return [h, s, l];
  }

  /**
   * Converts an HSL color value to RGB. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
   * Assumes h, s, and l are contained in the set [0, 1] and
   * returns r, g, and b in the set [0, 255].
   *
   * @param  h       The hue
   * @param  s       The saturation
   * @param  l       The lightness
   * @return  Array           The RGB representation
   */
  public hslToRgb(h: number, s: number, l: number) {
    let r;
    let g;
    let b;

    if (s === 0) {
      r = g = b = l; // achromatic
    } else {
      const hue2rgb = (n0: number, n1: number, n2: number) => {
        if (n2 < 0) {
          n2 += 1;
        }
        if (n2 > 1) {
          n2 -= 1;
        }
        if (n2 < 1 / 6) {
          return n0 + (n1 - n0) * 6 * n2;
        }
        if (n2 < 1 / 2) {
          return n1;
        }
        if (n2 < 2 / 3) {
          return n0 + (n1 - n0) * (2 / 3 - n2) * 6;
        }
        return n0;
      };

      const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
      const p = 2 * l - q;

      r = hue2rgb(p, q, h + 1 / 3);
      g = hue2rgb(p, q, h);
      b = hue2rgb(p, q, h - 1 / 3);
    }

    return [r * 255, g * 255, b * 255];
  }

  /**
   * Converts an RGB color value to HSV. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSV_color_space.
   * Assumes r, g, and b are contained in the set [0, 255] and
   * returns h, s, and v in the set [0, 1].
   *
   * @param  r       The red color value
   * @param  g       The green color value
   * @param  b       The blue color value
   * @return  Array           The HSV representation
   */
  public rgbToHsv(r: number, g: number, b: number) {
    r /= 255;
    g /= 255;
    b /= 255;

    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    let h;
    const v = max;

    const d = max - min;
    const s = max === 0 ? 0 : d / max;

    if (max === min) {
      h = 0; // achromatic
    } else {
      switch (max) {
        case r:
          h = (g - b) / d + (g < b ? 6 : 0);
          break;
        case g:
          h = (b - r) / d + 2;
          break;
        case b:
          h = (r - g) / d + 4;
          break;
      }

      h /= 6;
    }

    return [h, s, v];
  }

  /**
   * Converts an HSV color value to RGB. Conversion formula
   * adapted from http://en.wikipedia.org/wiki/HSV_color_space.
   * Assumes h, s, and v are contained in the set [0, 1] and
   * returns r, g, and b in the set [0, 255].
   *
   * @param  h       The hue
   * @param  s       The saturation
   * @param  v       The value
   * @return         The RGB representation
   */
  hsvToRgb(h: number, s: number, v: number) {
    let r;
    let g;
    let b;

    const i = Math.floor(h * 6);
    const f = h * 6 - i;
    const p = v * (1 - s);
    const q = v * (1 - f * s);
    const t = v * (1 - (1 - f) * s);

    switch (i % 6) {
      case 0:
        r = v;
        g = t;
        b = p;
        break;
      case 1:
        r = q;
        g = v;
        b = p;
        break;
      case 2:
        r = p;
        g = v;
        b = t;
        break;
      case 3:
        r = p;
        g = q;
        b = v;
        break;
      case 4:
        r = t;
        g = p;
        b = v;
        break;
      case 5:
        r = v;
        g = p;
        b = q;
        break;
    }

    return [r * 255, g * 255, b * 255];
  }

  rgbStringToValues(color: string) {
    const aRgbHex = color.match(/.{1,2}/g);
    return [
      parseInt(aRgbHex[0], 16),
      parseInt(aRgbHex[1], 16),
      parseInt(aRgbHex[2], 16)
    ];
  }

  rgbValuesToString(r: number, g: number, b: number) {
    const convert = (num) => {
      const str = (Math.round(num)).toString(16);
      return (str.length === 1 ? '0' : '') + str;
    };
    return `${convert(r)}${convert(g)}${convert(b)}`;
  }

  offsetHue(color: string, angle: number) {
    const values = this.rgbStringToValues(color);
    const hsvValues = this.rgbToHsv(values[0], values[1], values[2]);
    const rgbValues = this.hsvToRgb(hsvValues[0] + angle / 255, hsvValues[1], hsvValues[2]);
    return this.rgbValuesToString(rgbValues[0], rgbValues[1], rgbValues[2]);
  }

}
