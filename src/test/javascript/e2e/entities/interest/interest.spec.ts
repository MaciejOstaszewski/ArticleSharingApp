import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { InterestComponentsPage, InterestUpdatePage } from './interest.page-object';

describe('Interest e2e test', () => {
    let navBarPage: NavBarPage;
    let interestUpdatePage: InterestUpdatePage;
    let interestComponentsPage: InterestComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Interests', () => {
        navBarPage.goToEntity('interest');
        interestComponentsPage = new InterestComponentsPage();
        expect(interestComponentsPage.getTitle()).toMatch(/articleSharingApp.interest.home.title/);
    });

    it('should load create Interest page', () => {
        interestComponentsPage.clickOnCreateButton();
        interestUpdatePage = new InterestUpdatePage();
        expect(interestUpdatePage.getPageTitle()).toMatch(/articleSharingApp.interest.home.createOrEditLabel/);
        interestUpdatePage.cancel();
    });

    it('should create and save Interests', () => {
        interestComponentsPage.clickOnCreateButton();
        interestUpdatePage.setNameInput('name');
        expect(interestUpdatePage.getNameInput()).toMatch('name');
        interestUpdatePage.save();
        expect(interestUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
