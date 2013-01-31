//
//  SingleSelectListCell.m
//  MobileFieldData
//
//  Created by Chris Godwin on 3/12/12.
//
//

#import "SingleSelectListCell.h"
#import "SurveyAttributeOption.h"
#import <QuartzCore/QuartzCore.h>


@implementation SingleSelectListCell

@synthesize label, valueLabel, options, value;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier options:(NSArray*)o
{
    self = [super initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier];
    if (self) {
        
        label = [[UILabel alloc] initWithFrame:CGRectMake(10, 3, self.bounds.size.width-20, 15)];
        label.font = [UIFont boldSystemFontOfSize:12.0];
        label.numberOfLines = 0;
        [self.contentView addSubview:label];

        valueLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 20, self.bounds.size.width-35, 30)];
        valueLabel.font = [UIFont systemFontOfSize:12.0];
        valueLabel.numberOfLines = 2;
        valueLabel.text = @"";
        
        [self.contentView addSubview:valueLabel];
        
        NSArray *sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"weight" ascending:YES]];
        options = [o sortedArrayUsingDescriptors:sortDescriptors];
        
        value = [[NSMutableString alloc]init];
        self.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
 
}

- (void)setSelectedValue:(NSString*)inputValue
{
    if (inputValue != nil) {
            self.valueLabel.text = inputValue;
            [value setString: inputValue];
    }
}

-(NSString*)getSelectedValue
{
    return value;
}



@end