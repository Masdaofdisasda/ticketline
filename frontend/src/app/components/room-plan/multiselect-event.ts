import {Seat} from '../../dto/venue';

export class MultiselectEvent {
    added: Array<Seat>;
    removed: Array<Seat>;
    all: Array<Seat>;
}
