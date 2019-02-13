import { IInvite } from 'app/shared/model/invite.model';
import { IInterest } from 'app/shared/model/interest.model';
import { IInviteFromStragners } from 'app/shared/model/invite-from-strangers.model';

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    avatar?: string;
    interests?: IInterest[];
    invites?: IInvite[];
    friends?: IUser[];
    invitesFromStrangers?: IInviteFromStragners[];
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public avatar?: string,
        public interests?: IInterest[],
        public invites?: IInvite[],
        public friends?: IUser[],
        public invitesFromStrangers?: IInviteFromStragners[]
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.avatar = avatar ? avatar : null;
        this.interests = interests ? interests : null;
        this.friends = friends ? friends : null;
        this.invites = invites ? invites : null;
        this.invitesFromStrangers = invitesFromStrangers ? invitesFromStrangers : null;
    }
}
