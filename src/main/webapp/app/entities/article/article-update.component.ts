import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { IUser, UserService } from 'app/core';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { IInterest } from 'app/shared/model/interest.model';
import { InterestService } from 'app/entities/interest';

@Component({
    selector: 'jhi-article-form',
    templateUrl: './article-update.component.html'
})
export class ArticleUpdateComponent implements OnInit {
    private _article: IArticle;
    isSaving: boolean;

    users: IUser[];

    categories: ICategory[];

    tags: ITag[];

    interests: IInterest[];
    creationDateDp: any;
    modificationDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private articleService: ArticleService,
        private userService: UserService,
        private categoryService: CategoryService,
        private tagService: TagService,
        private interestService: InterestService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit(): void  {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ article }) => {
            this.article = article;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.interestService.query().subscribe(
            (res: HttpResponse<IInterest[]>) => {
                this.interests = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.article.id !== undefined) {
            this.subscribeToSaveResponse(this.articleService.update(this.article));
        } else {
            this.subscribeToSaveResponse(this.articleService.create(this.article, this.article.imageURL));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>) {
        result.subscribe((res: HttpResponse<IArticle>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    trackInterestById(index: number, item: IInterest) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get article() {
        return this._article;
    }

    set article(article: IArticle) {
        this._article = article;
    }
}
