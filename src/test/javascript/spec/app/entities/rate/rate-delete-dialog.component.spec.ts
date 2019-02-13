/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ArticleSharingAppTestModule } from '../../../test.module';
import { RateDeleteDialogComponent } from 'app/entities/rate/rate-delete-dialog.component';
import { RateService } from 'app/entities/rate/rate.service';

describe('Component Tests', () => {
    describe('Rate Management Delete Component', () => {
        let comp: RateDeleteDialogComponent;
        let fixture: ComponentFixture<RateDeleteDialogComponent>;
        let service: RateService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ArticleSharingAppTestModule],
                declarations: [RateDeleteDialogComponent]
            })
                .overrideTemplate(RateDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RateDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RateService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
