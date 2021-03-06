//
//  MultiSelectCell.m
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 25/09/12.
//
//

#import "MultiSelectCell.h"
#import "SurveyAttributeOption.h"

@implementation MultiSelectCell

@synthesize picker, options;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier options:(NSArray*)o
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        //picker = [[UIPickerView alloc]initWithFrame:CGRectMake(0, 50, self.bounds.size.width, 100)];
        picker = [[UIPickerView alloc]init];
        picker.frame = CGRectMake(0, 40, self.bounds.size.width, 162.0);
        picker.delegate = self;
        picker.dataSource = self;
        picker.backgroundColor = [UIColor whiteColor];
        picker.showsSelectionIndicator = NO;
        
        picker.transform = CGAffineTransformMakeScale(0.8, 0.8);

        [self.contentView addSubview:picker];
        
        NSArray *sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"weight" ascending:YES]];
        options = [o sortedArrayUsingDescriptors:sortDescriptors];
        
        selectedItems = [[NSMutableArray alloc]init];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setSelectedValues:(NSString*)inputValues
{
    NSArray *selectedArray = [inputValues componentsSeparatedByString:@","];
    
    for (int i=0; i<options.count; i++) {
        SurveyAttributeOption* option = [options objectAtIndex:i];
        if ([selectedArray containsObject:option.value]) {
            NSNumber *row = [NSNumber numberWithInt:i];
            [selectedItems addObject:row];
        }
    }
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.options.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    SurveyAttributeOption* option = [self.options objectAtIndex:row];
    return option.value;
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    UITableViewCell *cell = (UITableViewCell *)view;
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
        [cell setBackgroundColor:[UIColor clearColor]];
        [cell setBounds: CGRectMake(0, 0, cell.frame.size.width -20 , 44)];
        UITapGestureRecognizer *singleTapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(toggleSelection:)];
        singleTapGestureRecognizer.numberOfTapsRequired = 1;
        [cell addGestureRecognizer:singleTapGestureRecognizer];
    }
    
    if ([selectedItems indexOfObject:[NSNumber numberWithInt:row]] != NSNotFound) {
        [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
    } else {
        [cell setAccessoryType:UITableViewCellAccessoryNone];
    }
    SurveyAttributeOption* option = [self.options objectAtIndex:row];
    cell.textLabel.text = option.value;
    cell.tag = row;
    
    return cell;
}

- (void)toggleSelection:(UITapGestureRecognizer *)recognizer {
    NSNumber *row = [NSNumber numberWithInt:recognizer.view.tag];
    NSUInteger index = [selectedItems indexOfObject:row];
    if (index != NSNotFound) {
        [selectedItems removeObjectAtIndex:index];
        [(UITableViewCell *)(recognizer.view) setAccessoryType:UITableViewCellAccessoryNone];
    } else {
        [selectedItems addObject:row];
        [(UITableViewCell *)(recognizer.view) setAccessoryType:UITableViewCellAccessoryCheckmark];
    }
    
    NSMutableString* tmpValue = [[NSMutableString alloc]initWithString:@""];
    
    for (NSNumber* row in selectedItems) {
        SurveyAttributeOption* selectedOption = [options objectAtIndex:[row integerValue]];
        [tmpValue appendString:selectedOption.value];
        if ([selectedItems indexOfObject:row] != selectedItems.count-1) {
            [tmpValue appendString:@","];
        }
    }
    self.value = tmpValue;
}


@end
