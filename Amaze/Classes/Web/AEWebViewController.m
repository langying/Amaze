//
//  AEWebViewController.m
//  Amaze
//
//  Created by 韩琼 on 2017/2/4.
//  Copyright © 2017年 AppEngine. All rights reserved.
//

#import "AEView.h"
#import "AppTools.h"
#import "AEWebViewController.h"

#define TAG_INFO 111

@interface AEWebViewController()<AEViewDelegate>

@property(nonatomic, strong) NSString*      url;
@property(nonatomic, strong) AEView*        gview;
@property(nonatomic, strong) CADisplayLink* display;

@end

@implementation AEWebViewController

- (id)initWithURL:(NSString*)url {
    if ((self = [super init]) == nil) {
        return nil;
    }
    self.url = url;
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
    
    [self.view addSubview:({
        UIButton* btn = [UIButton.alloc initWithWidth:44 height:44];
        btn.backgroundColor = [UIColor clearColor];
        [btn setImage:[UIImage imageNamed:@"btn_back"] forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(home:) forControlEvents:UIControlEventTouchUpInside];
        btn;
    }) layout:UILayoutLT offset:CGSizeZero];
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

- (void)home:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - AEViewDelegate
- (void)view:(AEView*)view fps:(NSInteger)fps {
}
- (NSString*)view:(AEView*)view handle:(NSString*)text {
    return nil;
}

@end
