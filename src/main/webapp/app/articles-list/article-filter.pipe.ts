import { Injectable, Pipe, PipeTransform } from '@angular/core';
import { IArticle } from 'app/shared/model/article.model';

@Pipe({
    name: 'articleFilter'
})
@Injectable()
export class ArticleFilterPipe implements PipeTransform {
    transform(items: IArticle[]): any {
        if (items === undefined) return;
        return items.sort((a, b) => {
            if (a.views < b.views) {
                return 1;
            } else if (a.views > b.views) {
                return -1;
            } else {
                return 0;
            }
        });
    }
}
