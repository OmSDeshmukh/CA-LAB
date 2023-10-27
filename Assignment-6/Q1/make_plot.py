# import os
# import matplotlib.pyplot as plt

# data = []

# # Define a function to extract values from a text file
# def extract_values(file_path):
#     with open(file_path, 'r') as file:
#         lines = file.readlines()
#         for line in lines:
#             if line.startswith("Number of instructions executed"):
#                 instructions = int(line.split("=")[1].strip())
#             elif line.startswith("Number of cycles taken"):
#                 cycles = int(line.split("=")[1].strip())
#         if cycles != 0:
#             ipc = (instructions*1.0) / (cycles*1.0)
#         else:
#             ipc = 1
#     return ipc

# # Directory where your subfolders are located
# base_dir = '/Users/omdeshmukh/Downloads/SemV/CA-LAB/Assignment-6/Q1'

# # Traverse the directories and read the files
# for folder_name in os.listdir(base_dir):
#     folder_path = os.path.join(base_dir, folder_name)
#     if os.path.isdir(folder_path) and '_' in folder_name:
#         a, b = folder_name.split('_')
#         a, b = int(a), int(b)
#         ipc_values = []
#         for txt_file in os.listdir(folder_path):
#             if txt_file.endswith(".txt"):
#                 file_path = os.path.join(folder_path, txt_file)
#                 ipc = extract_values(file_path)
#                 ipc_values.append(ipc)
#         if ipc_values:
#             data.append((a / 4, sum(ipc_values)*1.0 / len(ipc_values)*1.0))

# # Extract x and y values for plotting
# x_values = [item[0] for item in data]
# y_values = [item[1] for item in data]

# # Create a plot
# plt.figure(figsize=(8, 6))
# plt.scatter(x_values, y_values, marker='o', c='b')
# plt.xlabel('a/4')
# plt.ylabel('Average IPC')
# plt.title('IPC vs a/4')
# plt.grid(True)
# plt.show()

import os
import matplotlib.pyplot as plt
import numpy as np

data = []

# Define a function to extract values from a text file
def extract_values(file_path):
    ipc_values = []
    with open(file_path, 'r') as file:
        lines = file.readlines()
        for line in lines:
            if line.startswith("Number of instructions executed"):
                instructions = int(line.split("=")[1].strip())
            elif line.startswith("Number of cycles taken"):
                cycles = int(line.split("=")[1].strip())
                if cycles != 0:
                    ipc = instructions*1.0 / cycles
                else:
                    ipc = 0
                ipc_values.append(ipc)
    return ipc_values

# Directory where your subfolders are located
base_dir = '/Users/omdeshmukh/Downloads/SemV/CA-LAB/Assignment-6/Q1'

# Traverse the directories and read the files
for folder_name in os.listdir(base_dir):
    folder_path = os.path.join(base_dir, folder_name)
    if os.path.isdir(folder_path) and '_' in folder_name:
        a, b = folder_name.split('_')
        a, b = int(a), int(b)
        for txt_file in os.listdir(folder_path):
            if txt_file.endswith(".txt"):
                file_path = os.path.join(folder_path, txt_file)
                ipc_values = extract_values(file_path)
                data.extend([(a / 4, ipc) for ipc in ipc_values])

# Extract x and y values for plotting
x_values = [item[0] for item in data]
y_values = [item[1] for item in data]

# Create a plot
plt.figure(figsize=(8, 6))
# print(y_values)
y_values1 = np.array(y_values).reshape((7,5)).T
# print(x_values)
x_values1 = [4 , 256 , 128 , 32 , 64 , 8 ,16]
# print(y_values1[0])

plt.plot(x_values1, y_values1[0], marker='o', c='blue')
plt.plot(x_values1, y_values1[1], marker='o', c='green')
plt.plot(x_values1, y_values1[2], marker='o', c='yellow')
plt.plot(x_values1, y_values1[3], marker='o', c='black')
plt.plot(x_values1, y_values1[4], marker='o', c='violet')
plt.xlabel('a/4')
plt.ylabel('IPC')
plt.title('IPC vs a/4')
plt.grid(True)
plt.show()
print(y_values1)

