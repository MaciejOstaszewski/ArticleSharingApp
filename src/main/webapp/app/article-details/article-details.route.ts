import { Routes } from '@angular/router';
import { ArticleResolve } from 'app/entities/article/article.route';
import { ArticleDetailsComponent } from 'app/article-details/article-details.component';

export class ArticleDetailsResolve {}
export const articleDetailsRoute: Routes = [
    {
        path: 'article/detail/:id',
        component: ArticleDetailsComponent,
        resolve: {
            article: ArticleResolve
        },
        data: {
            authorities: [],
            pageTitle: 'articleSharingApp.article.home.title'
        }
    }
];
