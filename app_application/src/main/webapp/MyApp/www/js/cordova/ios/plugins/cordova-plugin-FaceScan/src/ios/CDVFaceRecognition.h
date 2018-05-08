//
//  CDVFaceRecognition.h
//  贷我走
//
//  Created by 吴斌 on 2017/6/7.
//
//
/**                            *********
*                          如有疑问欢迎交流学习
*                  新浪微博：http://weibo.com/535478908
*         ********************************************************
*/
#import <Cordova/CDVPlugin.h>

@interface CDVFaceRecognition : CDVPlugin
- (void)faceScan:(CDVInvokedUrlCommand*)command;
@end
