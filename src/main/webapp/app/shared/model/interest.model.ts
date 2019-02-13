export interface IInterest {
    id?: number;
    name?: string;
}

export class Interest implements IInterest {
    constructor(public id?: number, public name?: string) {}
}
