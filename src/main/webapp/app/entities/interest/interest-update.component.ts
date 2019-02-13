import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IInterest } from 'app/shared/model/interest.model';
import { InterestService } from './interest.service';

@Component({
    selector: 'jhi-interest-update',
    templateUrl: './interest-update.component.html'
})
export class InterestUpdateComponent implements OnInit {
    private _interest: IInterest;
    isSaving: boolean;

    constructor(private interestService: InterestService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ interest }) => {
            this.interest = interest;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.interest.id !== undefined) {
            this.subscribeToSaveResponse(this.interestService.update(this.interest));
        } else {
            this.subscribeToSaveResponse(this.interestService.create(this.interest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IInterest>>) {
        result.subscribe((res: HttpResponse<IInterest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get interest() {
        return this._interest;
    }

    set interest(interest: IInterest) {
        this._interest = interest;
    }
}
