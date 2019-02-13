import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { CommentComponentsPage, CommentUpdatePage } from './comment.page-object';

describe('Comment e2e test', () => {
    let navBarPage: NavBarPage;
    let commentUpdatePage: CommentUpdatePage;
    let commentComponentsPage: CommentComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Comments', () => {
        navBarPage.goToEntity('comment');
        commentComponentsPage = new CommentComponentsPage();
        expect(commentComponentsPage.getTitle()).toMatch(/articleSharingApp.comment.home.title/);
    });

    it('should load create Comment page', () => {
        commentComponentsPage.clickOnCreateButton();
        commentUpdatePage = new CommentUpdatePage();
        expect(commentUpdatePage.getPageTitle()).toMatch(/articleSharingApp.comment.home.createOrEditLabel/);
        commentUpdatePage.cancel();
    });

    it('should create and save Comments', () => {
        commentComponentsPage.clickOnCreateButton();
        commentUpdatePage.setContentInput('content');
        expect(commentUpdatePage.getContentInput()).toMatch('content');
        commentUpdatePage.setCreationDateInput('2000-12-31');
        expect(commentUpdatePage.getCreationDateInput()).toMatch('2000-12-31');
        commentUpdatePage.setModificationDateInput('2000-12-31');
        expect(commentUpdatePage.getModificationDateInput()).toMatch('2000-12-31');
        commentUpdatePage.userSelectLastOption();
        commentUpdatePage.articleSelectLastOption();
        commentUpdatePage.save();
        expect(commentUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
