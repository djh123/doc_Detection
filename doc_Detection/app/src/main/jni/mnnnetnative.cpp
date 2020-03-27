//
//  mnnnetnative.cpp
//  MNN
//
//  Created by MNN on 2019/01/29.
//  Copyright © 2018, Alibaba Group Holding Limited
//

#include <android/bitmap.h>
#include <jni.h>
#include <string.h>
#include <MNN/ImageProcess.hpp>
#include <MNN/Interpreter.hpp>
#include <MNN/Tensor.hpp>
#include <memory>

static float X[25][19] = {
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},

        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},

        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},

        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},

        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368},
        {-0.947368,-0.842105,-0.736842,-0.631579,-0.526316,-0.421053,-0.315789,-0.210526,-0.105263,0,0.105263,0.210526,0.315789,0.421053,0.526316,0.631579,0.736842,0.842105,0.947368}
};

static float Y[25][19] = {
        {-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96,-0.96},
        {-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88,-0.88},
        {-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8,-0.8},
        {-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72,-0.72},
        {-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64,-0.64},

        {-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56,-0.56},
        {-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48,-0.48},
        {-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4,-0.4},
        {-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32,-0.32},
        {-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24,-0.24},

        {-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16,-0.16},
        {-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08,-0.08},
        {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        {0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08,0.08},
        {0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16,0.16},

        {0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24,0.24},
        {0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32,0.32},
        {0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4},
        {0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48,0.48},
        {0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56,0.56},

        {0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64,0.64},
        {0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72,0.72},
        {0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8},
        {0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88,0.88},
        {0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96,0.96},
};
#define  MAX(x,y)    ((x) > (y) ? (x) : (y))
static int wanted_input_width = 2048;
static int wanted_input_height = 1536;

void dsnt(int height, int width, float *heatmaps, int *X_coords, int *Y_coords) {
    // 1. norm the heatmaps.
    float exp_sum[] = {0,0,0,0};
    int dim = 4;
    for (int h=0; h<height; h++) {
        auto a_row = heatmaps + h * dim * width;
        for (int w=0; w<width; w++) {
            auto a_pos = a_row + w * dim;
            for (int d=0; d<dim; d++) {
                auto the_element = a_pos[d];
                a_pos[d] = exp(the_element);
                exp_sum[d] += a_pos[d];
            }
        }
    }

    for (int d=0; d<dim; d++) {
        exp_sum[d] = MAX(exp_sum[d], 1e-12);
    }

    for (int h=0; h<height; h++) {
        auto a_row = heatmaps + h * dim * width;
        for (int w=0; w<width; w++) {
            auto a_pos = a_row + w * dim;
            for (int d=0; d<dim; d++) {
                a_pos[d] /= exp_sum[d];
            }
        }
    }

    // 2. coordinate transform
    float x_coords[] = {0,0,0,0};
    float y_coords[] = {0,0,0,0};

    for (int h=0; h<height; h++) {
        auto X_row = X[h];
        auto Y_row = Y[h];
        auto _row = heatmaps + h * width * dim;
        for (int w=0; w<width; w++) {
            auto x_element = X_row[w];
            auto y_element = Y_row[w];
            auto _ele = _row + w*dim;
            for (int d=0; d<dim; d++) {
                x_coords[d] += (_ele[d] * x_element);
                y_coords[d] += (_ele[d] * y_element);
            }
        }
    }

    for (int d=0; d<dim; d++) {
        auto x_coord = x_coords[d];
        auto y_coord = y_coords[d];

        int x_real_coord = (int)round( (x_coord+1)/2.0 * wanted_input_height);
        int y_real_coord = (int)round( (y_coord+1)/2.0 * wanted_input_width);

        X_coords[d] = x_real_coord;
        Y_coords[d] = y_real_coord;
    }
}


MNN::Interpreter * interpreter;
MNN::Session* session;
MNN::CV::ImageProcess * process;
MNN::Tensor * nhwc_Tensor;
MNN::Tensor * inputTensor;

MNN::Tensor *  tensorOutHost;

MNN::Tensor *outTensor;


int outTensorW;
int outTensorH;
int outTensorC;

int inTensorW;
int inTensorH;
int inTensorC;


extern "C" JNIEXPORT jlong JNICALL
Java_com_taobao_android_mnn_MNNNetNative_nativeDocInit(JNIEnv *env, jclass type, jstring modelName_) {
    const char *modelName = env->GetStringUTFChars(modelName_, 0);
    interpreter = MNN::Interpreter::createFromFile(modelName);
    MNN::ScheduleConfig config;
    config.numThread = 4;
    config.type      = static_cast<MNNForwardType>(3);
    session = interpreter->createSession(config);

    MNN::CV::ImageProcess::Config cvconfig;
    cvconfig.sourceFormat = MNN::CV::ImageFormat::YUV_NV21;
    cvconfig.destFormat   = MNN::CV::ImageFormat::RGB;

    process  = MNN::CV::ImageProcess::create(cvconfig);



    inTensorW = 600;
    inTensorH = 800;
    inTensorC = 3;

    std::vector<int> dims{1,  inTensorH, inTensorW ,  inTensorC };
    nhwc_Tensor = MNN::Tensor::create<float>(dims, NULL, MNN::Tensor::TENSORFLOW);

    inputTensor  = interpreter->getSessionInput(session, nullptr);


    outTensorW = 25;
    outTensorH = 19;
    outTensorC = 4;
    std::vector<int> dimsout{1,  outTensorH, outTensorW ,  outTensorC };

    tensorOutHost= MNN::Tensor::create<float>(dimsout, NULL, MNN::Tensor::TENSORFLOW);

    outTensor = interpreter->getSessionOutput(session, nullptr);


    return 0;
}


extern "C" JNIEXPORT jlong JNICALL
Java_com_taobao_android_mnn_MNNNetNative_nativeDocDetection(JNIEnv *env, jclass type, jbyteArray jbufferData,jint srcW, jint srcH, jint srcC,jint dstW, jint dstH,jint dstC,jintArray outx, jintArray outy,jint degrees) {

    jint *outxp = env->GetIntArrayElements(outx, 0);
    jint *outyp = env->GetIntArrayElements(outy, 0);
    jbyte *bufferData = env->GetByteArrayElements(jbufferData, NULL);




    MNN::CV::Matrix trans;
    trans.setScale(1.0 / (srcW - 1), 1.0 / ( srcH - 1));
    trans.postRotate(degrees, 0.5, 0.5);
//    trans.postScale(-1.0,1.0, 0.5, 0.0);
    trans.postScale((dstW - 1), (dstH - 1));
    trans.invert(&trans);

    process->setMatrix(trans);

    process->convert((const unsigned char *)bufferData, srcW, srcH, 0, nhwc_Tensor);

    inputTensor->copyFromHostTensor(nhwc_Tensor);

    interpreter->runSession(session);

    outTensor->copyToHostTensor(tensorOutHost);
    auto output  = tensorOutHost->host<float>();

    auto output_h = 25;
    auto output_w = 19;
    auto output_dim = 4;
    float heatmaps[output_w*output_h*output_dim];

    ::memcpy(heatmaps, output, output_h * output_w * output_dim * sizeof(float));

    dsnt(output_h, output_w, heatmaps, outxp, outyp);

    env->ReleaseIntArrayElements(outx,outxp,0);
    env->ReleaseIntArrayElements(outy,outyp,0);
    env->ReleaseByteArrayElements(jbufferData,bufferData, 0);
    return 0;
}


extern "C" JNIEXPORT jlong JNICALL
Java_com_taobao_android_mnn_MNNNetNative_nativeDocDetectionRgb(JNIEnv *env, jclass type, jfloatArray jbufferData,jintArray outx, jintArray outy) {

    jint *outxp = env->GetIntArrayElements(outx, 0);
    jint *outyp = env->GetIntArrayElements(outy, 0);
    jfloat *bufferData = env->GetFloatArrayElements(jbufferData, NULL);

    auto nhwc_data   = nhwc_Tensor->host<float>();
    auto nhwc_size   = nhwc_Tensor->size();
    ::memcpy(nhwc_data, bufferData, nhwc_size);

    inputTensor->copyFromHostTensor(nhwc_Tensor);

    interpreter->runSession(session);





    outTensor->copyToHostTensor(tensorOutHost);
    auto output  = tensorOutHost->host<float>();

    auto output_h = 25;
    auto output_w = 19;

    auto output_dim = 4;

    float heatmaps[output_w*output_h*output_dim];

    std::memcpy(heatmaps, output, 25 * 19 * 4 * sizeof(float));

    dsnt(output_h, output_w, heatmaps, outxp, outyp);


    env->ReleaseIntArrayElements(outx,outxp,0);
    env->ReleaseIntArrayElements(outy,outyp,0);
    env->ReleaseFloatArrayElements(jbufferData,bufferData, 0);

    return 0;
}


extern "C" JNIEXPORT jlong JNICALL
Java_com_taobao_android_mnn_MNNNetNative_nativeDocRelease(JNIEnv *env, jclass type) {

    free(interpreter);
    free(session);
    free(nhwc_Tensor);
    free(tensorOutHost);
    free(inputTensor);
    free(outTensor);

    return 0;
}
