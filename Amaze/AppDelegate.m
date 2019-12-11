//
//  AppDelegate.m
//  Amaze
//
//  Created by 韩琼 on 16/1/21.
//  Copyright © 2016年 AppEngine. All rights reserved.
//

#import "AppDelegate.h"
#import "AEIndexViewController.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions {
    self.window = [UIWindow.alloc initWithFrame:[UIScreen mainScreen].bounds];
    self.window.backgroundColor = UIColor.whiteColor;
    self.window.rootViewController = [UINavigationController.alloc initWithRootViewController:AEIndexViewController.new];
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication*)application {
}

- (void)applicationDidEnterBackground:(UIApplication*)application {
}

- (void)applicationWillEnterForeground:(UIApplication*)application {
}

- (void)applicationDidBecomeActive:(UIApplication*)application {
}

- (void)applicationWillTerminate:(UIApplication*)application {
}

//- (void)updateBitmap:(NSString*)img1 bitmap:(NSString*)img2 {
//    UIImage* image1  = [UIImage imageNamed:img1];
//    UIImage* image2  = [UIImage imageNamed:img2];
//    GLubyte* bitmap1 = [self drawImage:image1];
//    GLubyte* bitmap2 = [self drawImage:image2];
//    for (uint idx = 0, len = image1.size.width * image1.size.height; idx < len; idx++) {
//        bitmap1[idx * 4 + 3] = 255 - bitmap2[idx * 4 + 0] / 2;
//    }
//    free(bitmap2);
//    GLuint width  = image1.size.width;
//    GLuint height = image1.size.height;
//    CGColorSpaceRef color   = CGColorSpaceCreateDeviceRGB();
//    CGContextRef    context = CGBitmapContextCreate(bitmap1, width, height, 8, width * sizeof(GLuint), color, kCGImageAlphaPremultipliedLast);
//    CGImageRef      cgimg   = CGBitmapContextCreateImage(context);
//    UIImage*        image   = [UIImage imageWithCGImage:cgimg];
//    CGContextRelease(context);
//    CGColorSpaceRelease(color);
//    NSData* data = UIImagePNGRepresentation(image);
//    NSString* filepath = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES)[0];
//    NSString* pathfile = [filepath stringByAppendingPathComponent:@"ret.png"];
//    [data writeToFile:pathfile atomically:YES];
//}
//
//- (GLubyte*)drawImage:(UIImage*)image {
//    CGImageRef cgimg  = image.CGImage;
//    GLuint     width  = (GLuint)CGImageGetWidth(cgimg);
//    GLuint     height = (GLuint)CGImageGetHeight(cgimg);
//    GLubyte*   pixels = (GLubyte*)calloc(width * height, sizeof(GLuint));
//
//    CGColorSpaceRef color   = CGColorSpaceCreateDeviceRGB();
//    CGContextRef    context = CGBitmapContextCreate(pixels, width, height, 8, width * sizeof(GLuint), color, kCGImageAlphaPremultipliedLast);
//    CGContextDrawImage(context, CGRectMake(0, 0, width, height), cgimg);
//    CGContextRelease(context);
//    CGColorSpaceRelease(color);
//    return pixels;
//}

@end
