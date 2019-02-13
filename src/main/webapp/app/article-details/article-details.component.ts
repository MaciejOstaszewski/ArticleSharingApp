import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article/article.service';
import { Account, IUser, LoginModalService, Principal, User, UserService } from 'app/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IRate, Rate } from 'app/shared/model/rate.model';
import { Observable } from 'rxjs/Rx';
import { HttpResponse } from '@angular/common/http';
import { AverageRate, IAverageRate } from 'app/shared/model/rating-average.model';
import { JhiEventManager } from 'ng-jhipster';
import { CategoryService } from 'app/entities/category';
import { Category, ICategory } from 'app/shared/model/category.model';

@Component({
    selector: 'jhi-article-details',
    templateUrl: './article-details.component.html',
    styleUrls: ['article-details.scss'],
    encapsulation: ViewEncapsulation.None
})
export class ArticleDetailsComponent implements OnInit, OnDestroy {
    article: IArticle;
    suggestedArticles: IArticle[];
    private _average: string = '';
    rateValue: number = 0;
    account: Account;
    disabled: boolean = false;
    rateButton: boolean = false;
    rated: boolean = false;
    modalRef: NgbModalRef;
    private _user: IUser = new User();
    private _category: ICategory = new Category();
    private _rate: IRate;
    private _averageRate: IAverageRate;

    constructor(
        private activatedRoute: ActivatedRoute,
        private articleService: ArticleService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private userService: UserService,
        private categoryService: CategoryService,
        private router: Router
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ article }) => {
            this.article = article;
            this.updateViews(this.article.id);
            this.getCategoryFromServer(this.article.categoryId);
            this.getUserFromServer(this.article.userLogin);
        });
        this.principal.identity().then(account => {
            this.account = account;
            this.checkIfUserRate(this.account);
        });
        this.registerAuthenticationSuccess();
        this.averageRate = new AverageRate();
        this.getAverageRate();
        this.getSuggestedArticles(this.article.id);
    }

    get user(): IUser {
        return this._user;
    }

    set user(value: IUser) {
        this._user = value;
    }

    get category(): ICategory {
        return this._category;
    }

    set category(value: ICategory) {
        this._category = value;
    }

    get rate(): IRate {
        return this._rate;
    }

    set rate(value: IRate) {
        this._rate = value;
    }

    get averageRate(): IAverageRate {
        return this._averageRate;
    }

    set averageRate(value: IAverageRate) {
        this._averageRate = value;
    }

    get average(): string {
        return this._average;
    }

    set average(value: string) {
        this._average = value;
    }

    getUserFromServer(login: string) {
        this.userService.find(login).subscribe(res => {
            this.user = res.body;
        });
    }

    getCategoryFromServer(id: number) {
        this.categoryService.find(id).subscribe(res => {
            this.category = res.body;
        });
    }

    subscribeToSaveResponse(result: Observable<HttpResponse<IRate>>) {
        result.subscribe((res: HttpResponse<IRate>) => {
            this.getAverageRate();
        });
    }

    updateViews(articleId: number) {
        this.articleService.updateViews(articleId).subscribe();
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy(): void {}

    getRateFromInput(event) {
        this.rateValue = event.rating;
    }

    checkIfUserRate(account: Account) {
        if (account != null) {
            this.articleService.getUserRating(this.account.login, this.article.id).subscribe((res: HttpResponse<IRate>) => {
                if (res != null) {
                    this.rateValue = res.body.value;
                    this.disabled = true;
                    this.rateButton = true;
                    this.rated = true;
                }
            });
        }
    }

    checkIfRateIsBiggerThanZero() {
        return this.rateValue === 0;
    }

    isUserIsLogged() {
        return this.account != null;
    }

    rateArticle() {
        if (this.isUserIsLogged()) {
            this.rate = new Rate(null, this.rateValue, this.article.id, null);
            this.subscribeToSaveResponse(this.articleService.addRating(this.rate));

            this.disabled = true;
            this.rateButton = true;
            this.rated = true;
        } else {
            this.login();
        }
    }

    getAverageRate() {
        this.articleService.getAverageRating(this.article.id).subscribe((res: HttpResponse<IAverageRate>) => {
            this.averageRate = res.body;
            console.log(this.averageRate);
            this.getAvgRating();
        });
    }

    getAvgRating() {
        if (this.averageRate.avgRate !== null) {
            this._average = this.averageRate.avgRate.toPrecision(2) + ' / ' + this.averageRate.rateAmount;
        }
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    loadFromCategory(id: any) {
        let waiting = '';
        if (!this.article.active) waiting = 'waiting';
        this.router.navigate([waiting, 'category', id]);
    }

    loadFromTag(id: any) {
        let waiting = '';
        if (!this.article.active) waiting = 'waiting';
        this.router.navigate([waiting, 'tag', id]);
    }

    private getSuggestedArticles(id: number) {
        this.articleService.getSuggestedArticles(id).subscribe((resp: HttpResponse<IArticle[]>) => {
            this.suggestedArticles = resp.body;
        });
    }
}
