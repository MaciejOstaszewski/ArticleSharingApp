import { Injectable, Pipe, PipeTransform } from '@angular/core';
import { IArticle } from 'app/shared/model/article.model';
import { IUser } from 'app/core';

@Pipe({
    name: 'filterFriends'
})
@Injectable()
export class FriendsFilterPipe implements PipeTransform {
    transform(items: IUser[], value: string): any {
        if (items === undefined) return;
        return items.filter(
            singleItem =>
                singleItem.login.toLowerCase().includes(value.toLowerCase()) ||
                singleItem.firstName.toLowerCase().includes(value.toLowerCase()) ||
                singleItem.lastName.toLowerCase().includes(value.toLowerCase())
        );
    }
}
