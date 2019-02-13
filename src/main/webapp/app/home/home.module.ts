import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ArticleSharingAppSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { UploadFileService } from 'app/shared/upload-file.service';
import { HttpClientModule } from '@angular/common/http';
import { ArticlesListModule } from 'app/articles-list/articles-list.module';

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticlesListModule, HttpClientModule, RouterModule.forChild(HOME_ROUTE)],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UploadFileService]
})
export class ArticleSharingAppHomeModule {}
