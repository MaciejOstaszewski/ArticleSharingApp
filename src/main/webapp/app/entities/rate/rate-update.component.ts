import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-rate-update',
    templateUrl: './rate-update.component.html'
})
export class RateUpdateComponent implements OnInit {
    private _rate: IRate;
    isSaving: boolean;

    articles: IArticle[];

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private rateService: RateService,
        private articleService: ArticleService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rate }) => {
            this.rate = rate;
        });
        this.articleService.query({ filter: 'rate-is-null' }).subscribe(
            (res: HttpResponse<IArticle[]>) => {
                if (!this.rate.articleId) {
                    this.articles = res.body;
                } else {
                    this.articleService.find(this.rate.articleId).subscribe(
                        (subRes: HttpResponse<IArticle>) => {
                            this.articles = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.rate.id !== undefined) {
            this.subscribeToSaveResponse(this.rateService.update(this.rate));
        } else {
            this.subscribeToSaveResponse(this.rateService.create(this.rate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRate>>) {
        result.subscribe((res: HttpResponse<IRate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackArticleById(index: number, item: IArticle) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get rate() {
        return this._rate;
    }

    set rate(rate: IRate) {
        this._rate = rate;
    }
}
