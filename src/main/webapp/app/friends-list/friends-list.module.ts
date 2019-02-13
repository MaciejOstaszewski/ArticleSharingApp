import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { friendsListRoute } from 'app/friends-list/friends-list.route';
import { FriendsListComponent } from 'app/friends-list/friends-list.component';
import { ArticleSharingAppAdminModule } from 'app/admin/admin.module';
import { ArticleSharingAppSharedModule } from 'app/shared';
import { FriendsFilterPipe } from 'app/friends-list/friends-filter.pipe';

const ENTITY_STATES = [...friendsListRoute];

@NgModule({
    imports: [ArticleSharingAppSharedModule, ArticleSharingAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [FriendsListComponent, FriendsFilterPipe],
    exports: [FriendsFilterPipe]
})
export class FriendsListModule {}
