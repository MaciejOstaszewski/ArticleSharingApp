import { IUser } from 'app/core';

export interface IInvite {
    id?: any;
    friend?: IUser;
}

export class Invite implements IInvite {
    constructor(public id?: any, public friend?: IUser) {
        this.id = id ? id : null;
        this.friend = friend ? friend : null;
    }
}
