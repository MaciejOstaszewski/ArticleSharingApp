import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { WaitingRoomComponent } from 'app/wiating-room/waiting-room.component';
import { UserRouteAccessService } from 'app/core';
import { HomeComponent } from 'app/home';

export class WaitingRoomResolve {}

export const waitingRoomRoute: Routes = [
    {
        path: 'waiting',
        component: WaitingRoomComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_REVIEWER'],
            defaultSort: 'id,asc',
            pageTitle: 'home.waiting'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'waiting/category/:categoryId',
        component: WaitingRoomComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.waiting'
        }
    },
    {
        path: 'waiting/tag/:tagId',
        component: WaitingRoomComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.waiting'
        }
    }
];
