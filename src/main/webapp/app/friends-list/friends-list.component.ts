import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account, IUser, Principal, UserService } from 'app/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-friends-list',
    templateUrl: './friends-list.component.html',
    styleUrls: ['friends-list.scss']
})
export class FriendsListComponent implements OnInit {
    account: Account;
    currentSearch: string;
    page: any;
    routeData: any;
    reverse: any;
    users: IUser[];

    searchMode: boolean = false;
    filter: string;

    constructor(
        private userService: UserService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private principal: Principal
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {});
        this.filter = '';
    }

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.loadFriends();
    }

    loadFriends() {
        this.userService
            .getFriends()
            .subscribe(
                (res: HttpResponse<IUser[]>) => this.getUsersFromResponse(res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadStrangers() {
        this.userService
            .getStrangers()
            .subscribe(
                (res: HttpResponse<IUser[]>) => this.getUsersFromResponse(res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/users',
            {
                page: this.page
            }
        ]);
        this.loadFriends();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;

        this.userService
            .search({
                query: this.currentSearch
            })
            .subscribe(
                (res: HttpResponse<IUser[]>) => this.paginateUsers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private paginateUsers(data: IUser[], headers: HttpHeaders) {
        this.users = data;
    }

    private getUsersFromResponse(data: IUser[]) {
        this.users = data;
    }

    private onError(errMsg: string) {
        this.jhiAlertService.error(errMsg, null, null);
    }

    invite(id: number) {
        this.userService.invite(id).subscribe(() => this.loadStrangers());
    }

    accept(id: number) {
        this.userService.acceptInvite(id).subscribe(() => {
            this.loadStrangers();
            this.principal.identity();
        });
    }

    checkUser(user: IUser) {
        return user.login === this.account.login;
    }

    loadLoggedUserFriends() {
        this.searchMode = false;
        this.loadFriends();
    }

    loadLoggedUserFriendsProposition() {
        this.searchMode = true;
        this.loadStrangers();
    }

    getCurrentlyLoggedUser() {
        return this.users.find(u => u.login === this.account.login);
    }

    delete(id: number) {
        this.userService.deleteFromFriends(id).subscribe(() => this.loadFriends());
    }

    checkInvites(user: IUser) {
        try {
            if (typeof this.getCurrentlyLoggedUser().invites != 'undefined') {
                if (this.getCurrentlyLoggedUser().invites.find(i => i.friend.login === user.login) !== undefined) {
                    return 1;
                }
            }
            if (this.getCurrentlyLoggedUser().invitesFromStrangers !== undefined) {
                if (this.getCurrentlyLoggedUser().invitesFromStrangers.find(i => i.friend.login === user.login) !== undefined) {
                    return 2;
                }
            }
        } catch {
            return 3;
        }

        return 3;
    }

    clearSearchFilter() {
        this.filter = '';
    }
}
