import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ArticleFormComponent } from 'app/article-form/article-form.component';
import { ArticleResolve } from 'app/entities/article/article.route';
import { ArticleFromEndComponent } from 'app/article-form/article-from-end.component';
export class ArticleFormResolve {}
export const articleFormRoute: Routes = [
    {
        path: 'article/form',
        component: ArticleFormComponent,
        resolve: {
            article: ArticleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'articleSharingApp.article.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'article/form/end',
        component: ArticleFromEndComponent,
        canActivate: [UserRouteAccessService]
    }
];
