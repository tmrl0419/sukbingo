#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      boondo
#
# Created:     22-06-2018
# Copyright:   (c) cse 2018
# Licence:     <your licence>
#-------------------------------------------------------------------------------

import argparse
import io
import os

from google.cloud import vision



# [START def_detect_labels]
def detect_labels(image_path):
    """Detects labels in the file."""
    result = {}
    file_name = os.path.join(
        os.path.dirname(__file__),
        image_path)

    client = vision.ImageAnnotatorClient()
    # [START migration_label_detection]
    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()
    image = vision.types.Image(content=content)
    response = client.label_detection(image=image)
    labels = response.label_annotations
    for label in labels:
        result[label.description] = label.score
    return result

    # [END migration_label_detection]
# [END def_detect_labels]
if __name__ == '__main__':
    BD ={}
    BD = detect_labels('resources/wakeupcat.jpg')
    for key, value in BD.items():
        print(key)

