import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ArticleSharingAppArticleModule } from './article/article.module';
import { ArticleSharingAppCommentModule } from './comment/comment.module';
import { ArticleSharingAppRateModule } from './rate/rate.module';
import { ArticleSharingAppInterestModule } from './interest/interest.module';
import { ArticleSharingAppCategoryModule } from './category/category.module';
import { ArticleSharingAppTagModule } from './tag/tag.module';
import { ArticleSharingAppMessageModule } from './message/message.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ArticleSharingAppArticleModule,
        ArticleSharingAppCommentModule,
        ArticleSharingAppRateModule,
        ArticleSharingAppInterestModule,
        ArticleSharingAppCategoryModule,
        ArticleSharingAppTagModule,
        ArticleSharingAppMessageModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticleSharingAppEntityModule {}
