//
//  AEIndexViewController.m
//  Amaze
//
//  Created by 韩琼 on 2017/2/4.
//  Copyright © 2017年 AppEngine. All rights reserved.
//

#import "AppTools.h"
#import "AEWebViewController.h"
#import "AEIndexViewController.h"

@interface AEIndexViewController()

@end

@implementation AEIndexViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    self.navigationController.navigationBar.hidden = YES;
    
    [self.view addSubview:({
        UIButton* btn = [UIButton.alloc initWithWidth:200 height:80 backgroudColor:0x00FF00FF];
        [btn setTitle:@"打开" font:20 color:0xFF0000FF bgImage:nil target:self action:@selector(onClick:)];
        btn;
    }) layout:UILayoutCenter offset:CGSizeZero];
}

- (void)onClick:(id)sender {
    AVAuthorizationStatus status = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    switch (status) {
        case AVAuthorizationStatusNotDetermined: {
            [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
                if (granted) {
                    [self open];
                }
                else {
                    // 用户明确拒绝授权
                }
            }];
            break;
        }
        case AVAuthorizationStatusAuthorized:{
            // 已经授权了
            [self open];
            break;
        }
        case AVAuthorizationStatusDenied:
        case AVAuthorizationStatusRestricted: {
            // 用户明确拒绝授权，或者相机设备无法访问
            break;
        }
        default: {
            break;
        }
    }
}

- (void)open {
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString* url = @"file:///asset/roller/index.js";
        [self.navigationController pushViewController:[[AEWebViewController alloc] initWithURL:url] animated:YES];
    });
}
@end
