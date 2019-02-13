import { ArticleSharingAppSharedModule } from 'app/shared';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import { RouterModule } from '@angular/router';
import { ArticleDetailsComponent } from 'app/article-details/article-details.component';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { ArticleFormComponent } from 'app/article-form/article-form.component';
import { articleDetailsRoute } from 'app/article-details/article-details.route';
import { ArticleDetailsCommentComponent } from 'app/article-details/article-details-comment/article-details-comment.component';
import { StarRatingModule } from 'angular-star-rating';

const ENTITY_STATES = [...articleDetailsRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES), StarRatingModule],
    declarations: [ArticleDetailsComponent, ArticleDetailsCommentComponent],
    entryComponents: [ArticleFormComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleDetailsModule {}
