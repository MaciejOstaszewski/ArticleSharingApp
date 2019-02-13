import { ArticlesListComponent } from 'app/articles-list/articles-list.component';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { ArticleSharingAppSharedCommonModule, ArticleSharingAppSharedLibsModule } from 'app/shared';
import { ArticleSharingAppAppRoutingModule } from 'app/app-routing.module';
import { ArticleFilterPipe } from 'app/articles-list/article-filter.pipe';

@NgModule({
    imports: [ArticleSharingAppSharedLibsModule, ArticleSharingAppSharedCommonModule, ArticleSharingAppAppRoutingModule],
    declarations: [ArticlesListComponent, ArticleFilterPipe],
    exports: [ArticlesListComponent, ArticleFilterPipe],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ArticlesListModule {}
