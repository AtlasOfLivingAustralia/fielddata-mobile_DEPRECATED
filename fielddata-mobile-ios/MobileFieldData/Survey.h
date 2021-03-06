//
//  Survey.h
//  MobileFieldData
//
//  Created by Birks, Matthew (CSIRO IM&T, Yarralumla) on 17/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import "SurveyAttribute.h"


@interface Survey : NSManagedObject

@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSDate * startDate;
@property (nonatomic, retain) NSDate * endDate;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * surveyDescription;
@property (nonatomic, retain) NSNumber * mapX;
@property (nonatomic, retain) NSNumber * mapY;
@property (nonatomic, retain) NSNumber * zoom;
@property (nonatomic, retain) NSDate * lastSync;
@property (nonatomic, retain) NSSet *attributes;
@property (nonatomic, retain) NSNumber *order;
@property (nonatomic, strong) NSArray* speciesIds;
@property (nonatomic, strong) NSString* imageUrl;
@property (nonatomic, retain) NSNumber * locationPolygon;
@property (nonatomic, retain) NSNumber * polygonCensusMethod;

@end

@interface Survey (CoreDataGeneratedAccessors)

- (void)addAttributesObject:(NSManagedObject *)value;
- (void)removeAttributesObject:(NSManagedObject *)value;
- (void)addAttributes:(NSSet *)values;
- (void)removeAttributes:(NSSet *)values;

// Returns the first attribute of the specified type.  Mostly useful for unique types
// (kSpeciesRP ,kNumber, kPointk, kLocation, kAccuracy, kWhen, kTimeRP, kNotes)
// If the survey contains more than one attribute with the same
// type, the first attribute encountered with that type will be
// returned.
-(SurveyAttribute *)getAttributeByType:(NSString *)attributeType;


// Returns the SurveyAttribute with the weight property equal to the supplied parameter.  The weight property is unique amongst SurveyAttributes of the same Survey.
// Return nil if no such SurveyAttribute exists.
-(SurveyAttribute *)getAttributeByWeight:(NSNumber *)attributeWeight;
@end
