import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { MessageComponentsPage, MessageUpdatePage } from './message.page-object';

describe('Message e2e test', () => {
    let navBarPage: NavBarPage;
    let messageUpdatePage: MessageUpdatePage;
    let messageComponentsPage: MessageComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Messages', () => {
        navBarPage.goToEntity('message');
        messageComponentsPage = new MessageComponentsPage();
        expect(messageComponentsPage.getTitle()).toMatch(/articleSharingApp.message.home.title/);
    });

    it('should load create Message page', () => {
        messageComponentsPage.clickOnCreateButton();
        messageUpdatePage = new MessageUpdatePage();
        expect(messageUpdatePage.getPageTitle()).toMatch(/articleSharingApp.message.home.createOrEditLabel/);
        messageUpdatePage.cancel();
    });

    it('should create and save Messages', () => {
        messageComponentsPage.clickOnCreateButton();
        messageUpdatePage.setContentInput('content');
        expect(messageUpdatePage.getContentInput()).toMatch('content');
        messageUpdatePage.setCreationDateInput('2000-12-31');
        expect(messageUpdatePage.getCreationDateInput()).toMatch('2000-12-31');
        messageUpdatePage.senderSelectLastOption();
        messageUpdatePage.receiverSelectLastOption();
        messageUpdatePage.save();
        expect(messageUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
