import { Moment } from 'moment';
import { ITag } from 'app/shared/model//tag.model';
import { IInterest } from 'app/shared/model//interest.model';

export interface IArticle {
    id?: number;
    title?: string;
    creationDate?: Moment;
    modificationDate?: Moment;
    content?: string;
    imageURL?: string;
    views?: number;
    active?: boolean;
    userLogin?: string;
    userId?: number;
    categoryId?: number;
    categoryName?: string;
    tags?: ITag[];
    interests?: IInterest[];
    images?: string[];
}

export class Article implements IArticle {
    constructor(
        public id?: number,
        public title?: string,
        public creationDate?: Moment,
        public modificationDate?: Moment,
        public content?: string,
        public imageURL?: string,
        public views?: number,
        public active?: boolean,
        public userLogin?: string,
        public userId?: number,
        public categoryId?: number,
        public categoryName?: string,
        public tags?: ITag[],
        public interests?: IInterest[],
        public images?: string[]
    ) {}
}
