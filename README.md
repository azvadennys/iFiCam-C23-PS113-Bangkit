# Machine Learning Documentation

## Transfer Learning InceptionV3
<p align="left">
 InceptionV3 is a deep learning model used for image classification, including the classification of ten types of fish. It is pre-trained on a large dataset and can extract relevant features from fish images. The model's architecture includes convolutional layers, pooling layers, and fully connected layers. It is trained using labeled fish images, and can classify new images by assigning probabilities to each fish species. InceptionV3 is effective for accurately identifying fish species based on visual characteristics.
</p>

## Dataset
<p align="left">
We obtained the dataset by photographing fish from the market, making it our original data. We augmented the dataset using Roboflow to enhance its diversity. The dataset was then split into a training set and a test set. The training set was used to train the InceptionV3 model, while the test set was used to evaluate its performance.
</p>
<p align="center">
  <img src="https://github.com/azvadennys/iFiCam-C23-PS113-Bangkit/blob/MachineLearning/Result/bar%20chart%20of%20train%20each%20category.png" alt="Deskripsi Gambar" style="width:100%;">
 <b>Dataset for Train and Validation</b>
</p>
<p align="center">
  <img src="https://github.com/azvadennys/iFiCam-C23-PS113-Bangkit/blob/MachineLearning/Result/bar%20chart%20of%20uji%20image%20each%20category.png" alt="Deskripsi Gambar" style="width:100%;">
 <b>Dataset for Test</b>
</p>

## Model Architecture
<p align="left">
InceptionV3 is a state-of-the-art deep learning model designed specifically for image classification tasks. It encompasses a sophisticated architecture comprising convolutional layers, pooling layers, and fully connected layers. This architecture enables the extraction of hierarchical features at varying levels of abstraction, facilitating the accurate classification of fish species.<br>
In addition to its primary architecture, InceptionV3 incorporates auxiliary classifiers at intermediate layers. These auxiliary classifiers contribute to gradient propagation during training, enhancing the overall performance and convergence speed of the model.
 </p>
 
## Training
Train the InceptionV3 model using the labeled images within the training set. Utilize prominent machine learning frameworks or libraries, such as TensorFlow or Keras, which provide pre-built implementations of InceptionV3. Fine-tune hyperparameters and training configurations based on experimentation and model performance.
### Pre-built implementations of InceptionV3
<code># Download the pre-trained weights. No top means it excludes the fully connected layer it uses for classification.
!wget --no-check-certificate \
    https://storage.googleapis.com/mledu-datasets/inception_v3_weights_tf_dim_ordering_tf_kernels_notop.h5 \
    -O /tmp/inception_v3_weights_tf_dim_ordering_tf_kernels_notop.h5</code>
### Fine-tune hyperparameters and Training configurations
| Type    | Value    |
|------------|------------|
| Learning Rate | <code>0.0001</code> | 
| Optimizer | <code>Adam</code> | 
| Batch Size | <code>32</code> | 
| Number of Training Epochs | <code>10</code> | 
| Input Shape | <code>(416,416,3)</code> | 
| Data Augmentation Parameters | <code>rescale=1./255,rotation_range=20,width_shift_range=0.2,</code><br><code>height_shift_range=0.2,shear_range=0.2,zoom_range=0.2,</code><br><code>fill_mode='nearest',brightness_range=[0.8, 1.2],horizontal_flip=True</code> | 
| Regularization Techniques |  <code>layers.Flatten()(last_output)</code><br><code>layers.Dense(1024, activation='relu')(x)</code><br><code>layers.Dropout(0.2)(x)</code><br><code>layers.Dense (10, activation='softmax')(x)</code><br> | 

## Evaluation and Visualitation
Once the model training is complete, evaluate its performance using the test set. Measure accuracy and other relevant evaluation metrics to assess the model's classification capability.

### Model Accuracy & Lose
<code>- loss: 0.0344 - accuracy: 0.9877 - val_loss: 0.0276 - val_accuracy: 0.9929</code>
<p align="center">
  <img src="https://github.com/azvadennys/iFiCam-C23-PS113-Bangkit/blob/MachineLearning/Result/accuration%20and%20loss.png" alt="Deskripsi Gambar" style="width:50%;">
</p>
##
