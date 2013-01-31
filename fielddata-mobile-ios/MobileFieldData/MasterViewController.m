//
//  MasterViewController.m
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 10/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "MasterViewController.h"
#import "LoginViewController.h"
#import "SpeciesListViewController.h"
#import "FieldDataService.h"
#import "SurveyViewController.h"
#import "SavedRecordsViewController.h"

@interface MasterViewController ()
- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;
@end

@implementation MasterViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = NSLocalizedString(@"Great Koala Count", @"Great Koala Count");
        
        preferences = [[Preferences alloc]init];
        
        UIImage* background = [MasterViewController imageWithImage:[UIImage imageNamed:@"background_image.jpg" ] scaledToSize:self.tableView.bounds.size ];
        self.tableView.backgroundColor = [UIColor colorWithPatternImage:background];
        self.tableView.backgroundView = [[UIImageView alloc] initWithImage:background];
        
        UILabel* header = [[UILabel alloc] initWithFrame:CGRectMake(10,10,self.tableView.bounds.size.width-20, 30)];
        header.textAlignment = NSTextAlignmentCenter;
        header.backgroundColor = [UIColor clearColor];
        header.text = [NSString stringWithFormat:@"Welcome %@", preferences.getUsersName];
        
        self.tableView.tableHeaderView = header;
    }
    return self;
}

+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize {
    //UIGraphicsBeginImageContext(newSize);
    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}
							
- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    //self.navigationItem.leftBarButtonItem = self.editButtonItem;

    //UIBarButtonItem *refreshButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(refreshFieldData:)];
    //self.navigationItem.rightBarButtonItem = refreshButton;
    
    
    // if the user is not logged on then redirect to the login page
    if ([preferences getFieldDataSessionKey] == NULL) {
        [self openLoginPage];
    }
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (void)refreshFieldData:(id)sender
{
    /*
    NSManagedObjectContext *context = [self.fetchedResultsController managedObjectContext];
    NSEntityDescription *entity = [[self.fetchedResultsController fetchRequest] entity];
    NSManagedObject *newManagedObject = [NSEntityDescription insertNewObjectForEntityForName:[entity name] inManagedObjectContext:context];
    
    // If appropriate, configure the new managed object.
    // Normally you should use accessor methods, but using KVC here avoids the need to add a custom class to the template.
    [newManagedObject setValue:[NSDate date] forKey:@"timeStamp"];
    
    // Save the context.
    NSError *error = nil;
    if (![context save:&error]) {
         // Replace this implementation with code to handle the error appropriately.
         // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. 
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }*/
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return surveys.count;
    }
    else if (section ==1) {
        return 3;
    }
    else if (section == 2) {
        return 1;
    }
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }

    [self configureCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)viewWillAppear:(BOOL)animated
{
    self.title = [preferences getPortalName];
    FieldDataService* fieldDataService = [[FieldDataService alloc] init];
    surveys = [fieldDataService loadSurveys];
    [self.tableView performSelectorOnMainThread:@selector(reloadData) withObject:nil waitUntilDone:NO];
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return NO;
}

- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // The table view should not be re-orderable.
    return NO;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    if(section == 0) {
        return @"Surveys";
    } else {
        return @"";
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        [self openSurveyPage:indexPath.row];
    }
    else if (indexPath.section == 1) {
        switch (indexPath.row) {
            case 0:
                [self openSavedRecordsPage];
                break;
            case 1:
                [self openWeblink:indexPath];
                
                break;
            case 2:
                [self openSpeciesPage];
                break;
            default:
                break;
        }
    }
    else if (indexPath.section == 2) {
        switch (indexPath.row) {
            case 0:
                [self openLoginPage];
                break;
            default:
                break;
        }
    }
}

-(void)openSavedRecordsPage
{
    SavedRecordsViewController *savedRecordsViewController =
        [[SavedRecordsViewController alloc] initWithStyle:UITableViewStylePlain];
    [self.navigationController pushViewController:savedRecordsViewController animated:YES];

    
}

-(void)openSurveyPage:(NSInteger) surveyIndex
{
    if (surveys != Nil && surveys.count != 0) {
        Survey* survey = [surveys objectAtIndex:surveyIndex];
        
        SurveyViewController* surveyViewController = [[SurveyViewController alloc] initWithStyle:UITableViewStylePlain
                                                                                          survey:survey
                                                                                          record:NULL];
        [self.navigationController pushViewController:surveyViewController animated:YES];
    }
    
}

- (void)openSpeciesPage
{
    SpeciesListViewController *speciesListViewController = [[SpeciesListViewController alloc] initWithStyle:UITableViewStylePlain];
    [self.navigationController pushViewController:speciesListViewController animated:YES];
}

-(void)openWeblink:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:NO];
    NSURL *url = [NSURL URLWithString:@"http://root.ala.org.au/bdrs-core/koalacount/map/mySightings.htm"];
    [[UIApplication sharedApplication] openURL:url];
}

- (void)openLoginPage
{
    LoginViewController *loginViewController = [[LoginViewController alloc] initWithNibName:@"LoginViewController" bundle:nil];
    if ([preferences getFieldDataSessionKey]) {
        [self presentModalViewController:loginViewController animated:YES];
    } else {
        [self presentModalViewController:loginViewController animated:NO];
    }
    //[self.navigationController pushViewController:loginViewController animated:YES];
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        Survey *survey = [surveys objectAtIndex:indexPath.row];
        cell.textLabel.text = survey.name;
    }
    else if (indexPath.section ==1) {
        switch (indexPath.row) {
    
            case 0:
                cell.textLabel.text = @"Saved Records";
                break;
            case 1:
                cell.textLabel.text = @"View My Records Online";
                break;
            case 2:
                cell.textLabel.text = @"Species List";
                break;
            default:
                break;
        }
    } else if (indexPath.section == 2) {
        switch (indexPath.row) {
            case 0:
                cell.textLabel.text = @"Change Login";
                break;
            //case 1:
            //    cell.textLabel.text = @"Settings";
            //    break;
            default:
                break;
        }
    }
    //NSManagedObject *object = [self.fetchedResultsController objectAtIndexPath:indexPath];
    //cell.textLabel.text = [[object valueForKey:@"timeStamp"] description];
}

@end