import { Moment } from 'moment';

export interface IComment {
    id?: number;
    content?: string;
    creationDate?: Moment;
    modificationDate?: Moment;
    userLogin?: string;
    userId?: number;
    articleId?: number;
}

export class Comment implements IComment {
    constructor(
        public id?: number,
        public content?: string,
        public creationDate?: Moment,
        public modificationDate?: Moment,
        public userLogin?: string,
        public userId?: number,
        public articleId?: number
    ) {}
}
