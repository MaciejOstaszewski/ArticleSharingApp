import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account, IUser, Principal, UserService } from 'app/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IMessage, Message } from 'app/shared/model/message.model';
import { Observable } from 'rxjs/index';
import { IMessageMerged } from 'app/shared/model/message-merged.model';

@Component({
    selector: 'jhi-messages-list',
    templateUrl: './messages.component.html',
    styleUrls: ['messages.scss']
})
export class MessagesComponent implements OnInit {
    account: Account;
    users: IUser[];
    messages: IMessageMerged[];
    private _messageContent: string;
    private _message: IMessage;
    public receiverId: number;
    private loggedUser: IUser;

    constructor(
        private userService: UserService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private principal: Principal
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            this.loadCurrentLoggedUser(this.account);
            this.checkIfAdmin();
        });

        this.loadFriends();
    }

    loadCurrentLoggedUser(account: Account) {
        this.userService.find(account.login).subscribe((res: HttpResponse<IUser>) => {
            this.getUserFromResponse(res.body);
        });
    }

    loadFriends() {
        this.userService.getFriends().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.getUsersFromResponse(res.body);
                this.setFocusOnFirstFriend();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private setFocusOnFirstFriend() {
        if (this.users) {
            this.receiverId = this.users[0].id;
            this.loadMessages(this.receiverId);
        }
    }

    private getUsersFromResponse(data: IUser[]) {
        this.users = data;
    }

    private getMessagesFromResponse(data: IMessageMerged[]) {
        this.messages = data;
    }

    private getUserFromResponse(data: IUser) {
        this.loggedUser = data;
    }

    private onError(errMsg: string) {
        this.jhiAlertService.error(errMsg, null, null);
    }

    checkUser(user: IUser) {
        return user.login === this.account.login;
    }

    send() {
        if (this.messageContent === '') return;
        this.message = new Message();
        this.message.content = this._messageContent;
        this.message.receiverId = this.receiverId;
        this.subscribeToSaveResponse(this.userService.saveUserMessage(this.message));
        this.messageContent = '';
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>) {
        result.subscribe(
            (res: HttpResponse<IMessage>) => {
                this.onSaveSuccess();
                this.reloadMessages();
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {}

    private onSaveError() {}

    reloadMessages() {
        this.loadMessages(this.receiverId);
    }

    loadMessages(id: number) {
        this.userService.loadUserMessages(id).subscribe((res: HttpResponse<IMessageMerged[]>) => {
            this.getMessagesFromResponse(res.body);
        });
    }

    get messageContent(): string {
        return this._messageContent;
    }

    set messageContent(value: string) {
        this._messageContent = value;
    }

    get message(): IMessage {
        return this._message;
    }

    set message(value: IMessage) {
        this._message = value;
    }

    setFocus(id: number) {
        this.receiverId = id;
        this.loadMessages(id);
    }

    isMyMessage(message: IMessageMerged): boolean {
        return message.ownerId === this.loggedUser.id;
    }

    isAnyMessage(): boolean {
        try {
            if (this.messages) {
                return this.messages.length > 0;
            } else {
                return false;
            }
        } finally {
        }
    }

    toggleFriend(id: number) {
        return id === this.receiverId;
    }

    quickSend(event) {
        if (event.keyCode === 13) {
            this.send();
        }
    }

    private checkIfAdmin() {
        if (this.account.login === 'admin') {
            this.userService.getAllUsers().subscribe(
                (res: HttpResponse<IUser[]>) => {
                    this.getUsersFromResponse(res.body);
                    this.setFocusOnFirstFriend();
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }
}
