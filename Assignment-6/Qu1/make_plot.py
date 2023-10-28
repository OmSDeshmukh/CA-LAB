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
base_dir = '/Users/omdeshmukh/Downloads/SemV/CA-LAB/Assignment-6/Qu1'

# Traverse the directories and read the files
for folder_name in os.listdir(base_dir):
    folder_path = os.path.join(base_dir, folder_name)
    if os.path.isdir(folder_path) and '_' in folder_name:
        a, b = folder_name.split('_')
        a, b = int(a), int(b)
        for txt_file in os.listdir(folder_path):
            if txt_file.endswith(".txt"):
                # print(txt_file)
                file_path = os.path.join(folder_path, txt_file)
                ipc_values = extract_values(file_path)
                data.extend([(a / 4, ipc) for ipc in ipc_values])

# Extract x and y values for plotting
x_values = [item[0] for item in data]
y_values = [item[1] for item in data]
# print(x_values)
# print(y_values)
# Create a plot
plt.figure(figsize=(8, 8))

y_values1 = np.array(y_values).reshape((4,5)).T

x_values1 = [4 , 256 , 128 , 32 ]
x_values1 =[16 , 128 , 512 , 1024]
# print(y_values1[0])
# print(x_values1)
color = ['red' , 'blue' , 'black' , 'green' , 'yellow']
labels = ['prime' , 'fibonacci' , 'descending' , 'evenorodd' , 'palindrome']
for i in range(5):
    plt.plot(x_values1, [y_values1[i][0] , y_values1[i][3] , y_values1[i][2] , y_values1[i][1] ], marker='o', c=color[i] , label = labels[i])
plt.legend()
plt.xlabel('size')
plt.ylabel('IPC')
plt.title('IPC vs L1i cache memory size(in bytes)')
plt.grid(True)
plt.xticks(x_values1)
# plt.yticks(y_values1.reshape(-1))
plt.show()
# print(y_values1)
# plt.savefig("q1.png")
print(x_values1)
print(y_values1)
