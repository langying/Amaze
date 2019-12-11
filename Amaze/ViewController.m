//
//  ViewController.m
//  Amaze
//
//  Created by 韩琼 on 16/2/2.
//  Copyright © 2016年 AppEngine. All rights reserved.
//

#import "ViewController.h"
#import "AEViewController.h"

@interface ViewController()<UITableViewDelegate, UITableViewDataSource>

@property(nonatomic, strong) NSMutableArray* items;

@end

@implementation ViewController

- (instancetype)init {
    if ((self = [super init]) == nil) {
        return nil;
    }
    self.items = [NSMutableArray array];
    [self.items addObject:@"http://tinyjs.alibaba.net/examples/games/running/index.js"];
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:({
        UITableView* table = [[UITableView alloc] initWithFrame:self.view.bounds style:UITableViewStylePlain];
        table.delegate   = self;
        table.dataSource = self;
        table.rowHeight  = 50;
        table.backgroundColor     = [UIColor whiteColor];
        table.sectionHeaderHeight = 22;
        table.separatorColor      = [UIColor lightGrayColor];
        table.keyboardDismissMode = UIScrollViewKeyboardDismissModeOnDrag;
        table;
    })];
}

#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView*)tableView didSelectRowAtIndexPath:(NSIndexPath*)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIViewController* controller = [[AEViewController alloc] initWithURL:self.items[indexPath.row]];
    [self.navigationController pushViewController:controller animated:YES];
}

#pragma mark - UITableViewDataSource
- (BOOL)tableView:(UITableView*)tableView canEditRowAtIndexPath:(NSIndexPath*)indexPath {
    return YES;
}
- (NSInteger)numberOfSectionsInTableView:(UITableView*)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView*)tableView numberOfRowsInSection:(NSInteger)section {
    return self.items.count;
}
- (UITableViewCell*)tableView:(UITableView*)tableView cellForRowAtIndexPath:(NSIndexPath*)indexPath {
    static NSString* CellId = @"CellId";
    UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:CellId];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellId];
        cell.accessoryType              = UITableViewCellAccessoryDisclosureIndicator;
        cell.backgroundColor            = [UIColor whiteColor];
        cell.textLabel.font             = [UIFont systemFontOfSize:18];
        cell.textLabel.textColor        = [UIColor blueColor];
        cell.textLabel.numberOfLines    = 0;
    }
    cell.textLabel.text = self.items[indexPath.row];
    return cell;
}

@end
