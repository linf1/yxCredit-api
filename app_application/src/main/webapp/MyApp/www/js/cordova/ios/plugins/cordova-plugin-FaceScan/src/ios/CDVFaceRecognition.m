//
//  CDVFaceRecognition.m
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

#import "CDVFaceRecognition.h"
#include <mach/mach_time.h>
//#import "WBProgressHUD.h"
#import "ZWCommonHttp.h"
#import "ISMessages.h"
#import <WBCloudFaceVerifySDK/WBFaceVerifyCustomerService.h>

#define WBFaceSignBaseUrlKey @"WBFaceSignBaseUrl"
#define WBFaceAppIDUrlKey @"WBFaceAppID"

#define kRandomLength 32

#define Licence @"WsIjSS+kNKVbXNQ8k5B9p/vgcQlQcKrWXZLo1oJBlNR0Zb+URLhMFJoURxjs8zj1q7qUDBfGm9Yp/EAhN5HkWHX6J96PGjJ+KjAEBI3UIUBNIh+LaHYNqIrsjf67aVAscDjlj07pBPFUkeOUpBFB0PvxJ/9JKp8GdmaCm5nx9ndccLQEA4Yl8Qgi+55jtqU7beJNDff+4GJ2rdQCrQ2fQNuxietVWAj0kVcLevpyeWRTJ5RprbhWSTMjM8ng8Al2uDUzMzpfdhQMJjE3fYshAVgStK64MUKFjBrstjPNm/DxCye2zv3Dq/7Cyx+YFrBN/TaxMO4GGYDYtqDGxVeuGg=="

#define iOS8Later ([UIDevice currentDevice].systemVersion.floatValue >= 8.0f)
//生成32位随机nonce
static const NSString *kRandomAlphabet=@"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
static NSString *creatUuid(){
    NSMutableString *randomString=[NSMutableString stringWithCapacity:kRandomLength];
    for (int i = 0; i < kRandomLength; i++) {
        [randomString appendFormat: @"%C", [kRandomAlphabet characterAtIndex:arc4random_uniform((u_int32_t)[kRandomAlphabet length])]];
    }
    //NSLog(@"randomString = %@", randomString);
    return randomString;
}

@interface CDVFaceRecognition ()<WBFaceVerifyCustomerServiceDelegate>
/** command */
@property (nonatomic,strong) CDVInvokedUrlCommand *command;
@end

@implementation CDVFaceRecognition
- (void)faceScan:(CDVInvokedUrlCommand*)command{
    _command = command;
    //进行验证
    [self startServiceWithType:WBFaceVerifyTypeMiddle 
                          name:command.arguments[0] 
                          idNo:command.arguments[1] 
                          sign:command.arguments[2] 
                         nonce:command.arguments[3]];
}

//开始活体验证
- (void)startServiceWithType:(WBFaceVerifyType)type name:(NSString *)name idNo:(NSString *)idNo sign:(NSString *)sign nonce:(NSString *)nonce{
    NSLog(@"start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    
    [WBFaceVerifyCustomerService sharedInstance].delegate = self;
    WBFaceUserInfo *userInfo = [[WBFaceUserInfo alloc] init];
    userInfo.name = name;
    userInfo.idNo = idNo;
    
    NSString *orderNO = [NSString stringWithFormat:@"orderNO%llu", mach_absolute_time()];
    NSString *userID = [NSString stringWithFormat:@"userID%llu", mach_absolute_time()];
    userInfo.orderNo = orderNO;
    userInfo.idType = @"01";

    /*
     测试环境
     
     APPID: appId001
     测试环境获取签名: https://ics-sitcloud.webank.com
     测试环境请求登录: https://sit-ida.webank.com
     
     生产环境
     
     APPID: TIDA0001
     测试环境获取签名: https://ida.webank.com 
     测试环境请求登录: https://ida.webank.com
     
     */
//    NSString *signbaseUrl = [[[NSBundle mainBundle] infoDictionary] objectForKey:WBFaceSignBaseUrlKey];
//    signbaseUrl = signbaseUrl ? : @"https://ida.webank.com";
    NSString *appid = [[[NSBundle mainBundle] infoDictionary] objectForKey:WBFaceAppIDUrlKey];
    appid = appid?:@"TIDA0001";
    /**************************************************************************************************************************************************************************************************************************************
     
     注意:  
     
     1. 合作方iOS端不要调用 '/ems-partner/cert/signature' 这个接口去获取签名sign.
     2. 请通过你们自己的后台获取sign,nonce,userID参数(参数生成算法要求见word文档).iOS层面只需要透传这几个参数.
     3. 本demo中app的测试环境在公网不通,demo是无法运行的,请按照下一条要求,demo可以正常运行
     4. 在app的info.plist文件中WBFaceBaseUrl,WBFaceSignBaseUrl,WBFaceAppID.三个字段请注释掉,且
     app的bundleID不能修改,app在正式环境即可运行
     5. 合作方接入SDK时, 需要使用自己的app的bundleID,以及相对应的licence字符串.请求参数中sign, nonce, userID请透传后台生成的数据即可
     ***************************************************************************************************************************************************************************/
    
        if (sign == nil || [sign isKindOfClass:[NSNull class]]) {
            if (iOS8Later) {
                [ISMessages showCardAlertWithTitle:@"获取签名出错" message:@"获取签名时候,后台返回参数出错" iconImage:nil
                                          duration:3 hideOnSwipe:YES hideOnTap:YES alertType:ISAlertTypeError alertPosition:0];
            }
            return ;
        }else{
            /**************************************************************************************************************************************************************************************************************************************
             
             注意:
             合作方只需要调用如下方法即可进入sdk
             ***************************************************************************************************************************************************************************/
            [[WBFaceVerifyCustomerService sharedInstance] startWBFaceServiceWithUserid:userID nonce:nonce sign:sign appid:appid userInfo:userInfo apiVersion:@"1.0.0" faceverifyType:type licence:Licence success:^{
                // 进入到这一步表示登录成功,会进入正视人脸验证的界面
                //                [self hideLoading];
                
            } failure:^(WBFaceVerifyLogin errorCode, NSString * _Nonnull faceCode, NSString * _Nonnull message) {
                //                [self hideLoading];
                // 进入到这里表示登录出错,具体原因见message
                if (iOS8Later) {
                    [ISMessages showCardAlertWithTitle:@"登录时候出错" message:message iconImage:nil
                                              duration:3 hideOnSwipe:YES hideOnTap:YES alertType:ISAlertTypeError alertPosition:0];
                }
            }];
        }
} 

#pragma mark - WBFaceVerifyCustomerServiceDelegate
-(void)wbfaceVerifyCustomerServiceDidFinished:(WBFaceVerifySDKErrorCode)errorCode faceCode:(NSString *)faceCode faceMsg:(NSString *)message sign:(NSString *)sign{
    NSString *msg = [NSString stringWithFormat:@"第三方获取到退出信息: errorCode:%ld faceCode:%@ message:%@ ,sign:%@",(long)errorCode, faceCode, message, sign];
    NSLog(@"%@",msg);
    
    if (errorCode == WBFaceVerifySDKErrorCode_SUCESS) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"认证成功"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:_command.callbackId];
    }else{
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"认证失败"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:_command.callbackId];
    }
    
}

-(UIViewController *)wbfaceVerifyServiceGetViewController:(WBFaceVerifyCustomerService *)service{
    return [UIApplication sharedApplication].keyWindow.rootViewController;
}

@end
