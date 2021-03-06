//
//  SpeciesCell.m
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 28/09/12.
//
//

#import "SpeciesCell.h"
#import "Species.h"
#import "SurveyAttributeOption.h"

@implementation SpeciesCell

@synthesize picker, speciesImage, species;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier species:(NSArray*)s
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        species = s;
        
        Species* firstSpecies = [species objectAtIndex:0];
        
        speciesImage=[[UIImageView alloc] initWithFrame:CGRectMake(10, 30, 96, 96)];
        speciesImage.autoresizingMask = ( UIViewAutoresizingNone );
        speciesImage.autoresizesSubviews = NO;
        [self.contentView addSubview:speciesImage];
        
        UIImage* image = [UIImage imageWithContentsOfFile:firstSpecies.imageFileName];
        [speciesImage setImage:image];
        
        picker = [[UIPickerView alloc]init];
        picker.frame = CGRectMake(110, 14, 200, 162.0);
        picker.delegate = self;
        picker.dataSource = self;
        picker.backgroundColor = [UIColor whiteColor];
        picker.showsSelectionIndicator = YES;
        picker.transform = CGAffineTransformMakeScale(0.8, 0.8);
        [self.contentView addSubview:picker];

        self.value= firstSpecies.commonName;
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return species.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    Species* s = [species objectAtIndex:row];
    return s.commonName;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    
    Species* s = [species objectAtIndex:row];
    self.value = s.commonName;
    
    UIImage* image = [UIImage imageWithContentsOfFile:s.imageFileName];
    [speciesImage setImage:image];
}

@end
