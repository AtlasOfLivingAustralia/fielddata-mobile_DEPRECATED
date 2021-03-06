//
//  MultiSelectCell.h
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 25/09/12.
//
//

#import <UIKit/UIKit.h>
#import "SurveyInputCell.h"

@interface MultiSelectCell : SurveyInputCell <UIPickerViewDelegate, UIPickerViewDataSource> {
    @private
    NSMutableArray *selectedItems;
}

@property (nonatomic, retain) UIPickerView* picker;
@property (nonatomic, retain) NSArray* options;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier options:(NSArray*)o;
- (void)setSelectedValues:(NSString*)inputValues;

@end
