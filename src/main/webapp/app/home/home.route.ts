import { Route, Routes } from '@angular/router';

import { HomeComponent } from './';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const HOME_ROUTE: Routes = [
    {
        path: '',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.title'
        }
    },
    {
        path: 'category/:categoryId',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.title'
        }
    },
    {
        path: 'tag/:tagId',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.title'
        }
    },
    {
        path: 'search',
        component: HomeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: [],
            defaultSort: 'id,asc',
            pageTitle: 'home.title'
        }
    }
];
