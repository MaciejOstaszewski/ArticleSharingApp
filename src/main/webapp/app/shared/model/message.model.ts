import { Moment } from 'moment';

export interface IMessage {
    id?: number;
    content?: string;
    creationDate?: Moment;
    receiverId?: number;
    read?: boolean;
}

export class Message implements IMessage {
    constructor(
        public id?: number,
        public content?: string,
        public creationDate?: Moment,
        public receiverId?: number,
        public read?: boolean
    ) {}
}
