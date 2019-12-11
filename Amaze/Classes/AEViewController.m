//
//  AEViewController.m
//  Action
//
//  Created by 韩琼 on 16/1/12.
//  Copyright © 2016年 AppEngine. All rights reserved.
//

#import "AEView.h"
#import "AppTools.h"
#import "CMPopTipView.h"
#import "AEViewController.h"

#define TAG_INFO 111

@interface AEViewController()<AEViewDelegate, CMPopTipViewDelegate>

@property(nonatomic, assign) NSInteger      idx;
@property(nonatomic, strong) NSString*      url;
@property(nonatomic, strong) AEView*        gview;
@property(nonatomic, strong) CMPopTipView*  pupop;
@property(nonatomic, strong) NSArray*       planets;
@property(nonatomic, strong) CADisplayLink* display;

@end

@implementation AEViewController

- (id)initWithURL:(NSString*)url {
    if ((self = [super init]) == nil) {
        return nil;
    }
    self.url = url;
    NSError* error;
    NSData* data = [NSData dataWithContentsOfFile:[NSBundle.mainBundle pathForResource:@"solar/info.json" ofType:nil]];
    self.planets = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:({
        AEView* gview = [[AEView alloc] initWithFrame:self.view.bounds];
        gview.handle = self;
        self.gview = gview;
    })];
    
    CGSize size = self.view.bounds.size;
    [self.view addSubview:({
        UIButton* btn = [UIButton.alloc initWithFrame:CGRectMake(0, size.height - 44, 44, 44)];
        btn.tag = TAG_INFO;
        btn.backgroundColor = [UIColor clearColor];
        [btn setImage:[UIImage imageNamed:@"btn_info"] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(info:) forControlEvents:UIControlEventTouchUpInside];
        btn;
    })];
    
    [self.view addSubview:({
        UIButton* btn = [UIButton.alloc initWithFrame:CGRectMake(size.width - 44, size.height - 44, 44, 44)];
        btn.backgroundColor = [UIColor clearColor];
        [btn setImage:[UIImage imageNamed:@"btn_back"] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(home:) forControlEvents:UIControlEventTouchUpInside];
        btn;
    })];
    
    [self.view addSubview:({
        UIButton* btn = [UIButton.alloc initWithFrame:CGRectMake(size.width - 44, 0, 44, 44)];
        btn.backgroundColor = [UIColor clearColor];
        [btn setImage:[UIImage imageNamed:@"btn_init"] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(reset:) forControlEvents:UIControlEventTouchUpInside];
        btn;
    })];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [_gview loadURL:_url];
    _display = [CADisplayLink displayLinkWithTarget:_gview selector:@selector(display)];
    _display.frameInterval = 1;
    [_display addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.display invalidate];
    self.display = nil;
}

- (void)info:(id)sender {
    NSDictionary* info = _planets[_idx];
    CMPopTipView *pupop = [[CMPopTipView alloc] initWithTitle:info[@"name"] message:info[@"info"]];
    pupop.maxWidth              = self.view.bounds.size.width / 2;
    pupop.delegate              = self;
    pupop.hasShadow             = NO;
    pupop.has3DStyle            = NO;
    pupop.borderWidth           = 0;
    pupop.textFont              = [UIFont systemFontOfSize:12];
    pupop.titleFont             = [UIFont systemFontOfSize:14];
    pupop.cornerRadius          = 4;
    pupop.textAlignment         = NSTextAlignmentLeft;
    pupop.backgroundColor       = [UIColor RGBAColor:0x4949498F];
    pupop.dismissTapAnywhere    = YES;
    pupop.hasGradientBackground = NO;
    [pupop presentPointingAtView:[self.view viewWithTag:TAG_INFO] inView:self.view animated:YES];
    self.pupop = pupop;
}
- (void)home:(id)sender {
    [_gview evalute:@"menu_home();"];
}
- (void)reset:(id)sender {
    [_gview evalute:@"menu_reset();"];
}

#pragma mark - AEViewDelegate
- (void)view:(AEView*)view fps:(NSInteger)fps {
}
- (NSString*)view:(AEView*)view handle:(NSString*)text {
    _idx = [text integerValue];
    return nil;
}

#pragma mark - CMPopTipViewDelegate
- (void)popTipViewWasDismissedByUser:(CMPopTipView*)popTipView {
}

@end
