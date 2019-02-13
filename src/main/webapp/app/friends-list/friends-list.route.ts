import { Routes } from '@angular/router';
import { FriendsListComponent } from 'app/friends-list/friends-list.component';
import { UserRouteAccessService } from 'app/core';

export class FriendsListRoute {}

export const friendsListRoute: Routes = [
    {
        path: 'user/friends',
        component: FriendsListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articleSharingApp.friends.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
