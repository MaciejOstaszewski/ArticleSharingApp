import { Moment } from 'moment';

export interface ICommentWithUser {
    id?: number;
    content?: string;
    creationDate?: Moment;
    modificationDate?: Moment;
    userNick?: string;
    userAvatar?: string;
}

export class CommentWithUser implements ICommentWithUser {
    constructor(
        public id?: number,
        public content?: string,
        public creationDate?: Moment,
        public modificationDate?: Moment,
        public userNick?: string,
        public userAvatar?: string
    ) {}
}
