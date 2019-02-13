import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRate } from 'app/shared/model/rate.model';
import { Principal } from 'app/core';
import { RateService } from './rate.service';

@Component({
    selector: 'jhi-rate',
    templateUrl: './rate.component.html'
})
export class RateComponent implements OnInit, OnDestroy {
    rates: IRate[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rateService: RateService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rateService
                .search({
                    query: this.currentSearch
                })
                .subscribe((res: HttpResponse<IRate[]>) => (this.rates = res.body), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.rateService.query().subscribe(
            (res: HttpResponse<IRate[]>) => {
                this.rates = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRates();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRate) {
        return item.id;
    }

    registerChangeInRates() {
        this.eventSubscriber = this.eventManager.subscribe('rateListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
