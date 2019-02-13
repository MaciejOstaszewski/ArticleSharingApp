export interface IRate {
    id?: number;
    value?: number;
    articleId?: number;
    userId?: number;
}

export class Rate implements IRate {
    constructor(public id?: number, public value?: number, public articleId?: number, public userId?: number) {}
}
