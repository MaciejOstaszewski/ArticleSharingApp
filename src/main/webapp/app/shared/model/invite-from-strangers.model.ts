import { IUser } from 'app/core';

export interface IInviteFromStragners {
    id?: any;
    friend?: IUser;
    read?: boolean;
}

export class InviteFromStragners implements IInviteFromStragners {
    constructor(public id?: any, public friend?: IUser, public read?: boolean) {
        this.id = id ? id : null;
        this.friend = friend ? friend : null;
        this.read = read ? read : null;
    }
}
