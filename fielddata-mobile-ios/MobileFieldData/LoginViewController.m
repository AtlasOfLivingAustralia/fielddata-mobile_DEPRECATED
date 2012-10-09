//
//  LoginViewController.m
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 10/09/12.
//  Copyright (c) 2012 CSIRO. All rights reserved.
//

#import "LoginViewController.h"
#import "RFRequest.h"
#import "RFResponse.h"
#import "RFService.h"
#import "Preferences.h"
#import "AlertService.h"

@interface LoginViewController ()

@end

@implementation LoginViewController

@synthesize username, password;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        preferences = [[Preferences alloc]init];
        fieldDataService = [[FieldDataService alloc]init];
        fieldDataService.delegate = self;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
    self.username = nil;
    self.password = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (IBAction)onClickLogin:(id)sender
{
    [username resignFirstResponder];
    [password resignFirstResponder];
    
    // make the query execute on another thread so that the progress indicator will be shown....
    dispatch_queue_t dispatchQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_async(dispatchQueue, ^(void){
        
        dispatch_sync(dispatch_get_main_queue(), ^{
            // Show the progress indicator
            [self showProgressIndicator];
        });
        
        dispatch_sync(dispatch_get_main_queue(), ^{
        
            NSString *portalName = [preferences getPortalName];
            
            //[LoginService doLogin:self.username.text password:self.password.text portalName:portalName];
            
            NSString *url = [preferences getFieldDataURL];
            
            RFRequest *r = [RFRequest requestWithURL:[NSURL URLWithString:url] type:RFRequestMethodPost
                              resourcePathComponents:@"survey", @"login", nil];
            
            [r addParam:portalName forKey:@"portalName"];
            [r addParam:self.username.text forKey:@"username"];
            [r addParam:self.password.text forKey:@"password"];
            
            //now execute this request and fetch the response in a block
            [RFService execRequest:r completion:^(RFResponse *response){
                [self handleLoginResponse:response];
            }];
        });
    });
}

- (void)handleLoginResponse:(RFResponse*)response
{
    NSLog(@"%@", response); //print out full response
   
    NSError *error;
    NSDictionary *dictionary = [NSJSONSerialization JSONObjectWithData:response.dataValue 
                                                               options:kNilOptions error:&error];
    NSString *ident = [dictionary valueForKey:@"ident"];
    NSLog(@"%@", ident);
    
    
    
    if (ident == NULL) {
        [self hideProgressIndicator];
        [AlertService DisplayMessageWithTitle:@"Login Failed" message:@"Username or Password not recognised"];
    } else {
        [preferences setFieldDataSessionKey:ident];
        
        // delete all the existing entities
        [fieldDataService deleteAllEntities:@"Species"];
        [fieldDataService deleteAllEntities:@"Survey"];
        
        // download the new surveys
        [fieldDataService downloadSurveys];
    }
}

- (void)downloadSurveysSuccessful:(BOOL)success surveyArray:(NSArray*)surveys {
    
    if (!success) {
        [self hideProgressIndicator];
        [AlertService DisplayMessageWithTitle:@"Network Error" message:@"Downloading Field Data surveys failed."];
    } else {
        
        numSurveys = [surveys count];
        
        for (NSDictionary* survey in surveys) {
         
            NSString* surveyId = [survey objectForKey:@"id"];
            [fieldDataService downloadSurveyDetails:surveyId];
        }
    }
}

- (void)downloadSurveyDetailsSuccessful:(BOOL)success survey:(NSDictionary*)survey {
    
    surveyCount++;
    if (surveyCount >= numSurveys) {
        [self hideProgressIndicator];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void)showProgressIndicator
{
    // add the progress indicator to the view
    progressIndicator = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    // Set properties
    progressIndicator.labelText = @"Logging in";
}

-(void)hideProgressIndicator
{
    [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

@end