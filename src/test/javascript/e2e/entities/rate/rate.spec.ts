import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { RateComponentsPage, RateUpdatePage } from './rate.page-object';

describe('Rate e2e test', () => {
    let navBarPage: NavBarPage;
    let rateUpdatePage: RateUpdatePage;
    let rateComponentsPage: RateComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Rates', () => {
        navBarPage.goToEntity('rate');
        rateComponentsPage = new RateComponentsPage();
        expect(rateComponentsPage.getTitle()).toMatch(/articleSharingApp.rate.home.title/);
    });

    it('should load create Rate page', () => {
        rateComponentsPage.clickOnCreateButton();
        rateUpdatePage = new RateUpdatePage();
        expect(rateUpdatePage.getPageTitle()).toMatch(/articleSharingApp.rate.home.createOrEditLabel/);
        rateUpdatePage.cancel();
    });

    it('should create and save Rates', () => {
        rateComponentsPage.clickOnCreateButton();
        rateUpdatePage
            .getValueInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    rateUpdatePage.getValueInput().click();
                    expect(rateUpdatePage.getValueInput().isSelected()).toBeFalsy();
                } else {
                    rateUpdatePage.getValueInput().click();
                    expect(rateUpdatePage.getValueInput().isSelected()).toBeTruthy();
                }
            });
        rateUpdatePage.articleSelectLastOption();
        rateUpdatePage.userSelectLastOption();
        rateUpdatePage.save();
        expect(rateUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
