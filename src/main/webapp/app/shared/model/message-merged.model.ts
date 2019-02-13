import { Moment } from 'moment';

export interface IMessageMerged {
    id?: number;
    content?: string;
    creationDate?: Moment;
    read?: boolean;
    ownerId?: number;
}

export class MessageMerged implements IMessageMerged {
    constructor(
        public id?: number,
        public content?: string,
        public creationDate?: Moment,
        public read?: boolean,
        public ownerId?: number
    ) {}
}
