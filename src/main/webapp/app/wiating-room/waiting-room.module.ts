import { ArticleSharingAppSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { UploadFileService } from 'app/shared/upload-file.service';
import { WaitingRoomComponent } from 'app/wiating-room/waiting-room.component';
import { waitingRoomRoute } from 'app/wiating-room/waiting-room.route';
import { ArticlesListModule } from 'app/articles-list/articles-list.module';

const ENTITY_STATES = [...waitingRoomRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticlesListModule, HttpClientModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [WaitingRoomComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UploadFileService]
})
export class WaitingRoomModule {}
