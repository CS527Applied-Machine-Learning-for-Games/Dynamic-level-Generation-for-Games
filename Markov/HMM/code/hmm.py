import argparse
import sys
import base
import numpy as np
import os


class MMG:
    def __init__(self):
        pass

    def getParams(self):
        args = argparse.ArgumentParser(
            description="Input map"
        )
        args.add_argument(
            "-ns",
            "--num-states",
            type=int,
            required=True
        )

        args.add_argument(
            "-rm",
            "--req-maps",
            type=int,
            required=True
        )

        args.add_argument(
            "in_map",
            type=argparse.FileType("r"),
            default=sys.stdin
        )

        args = args.parse_args()
        return args.num_states, args.req_maps, args.in_map

    def parseInput(self, input_map):
        # Parse map line by line
        map_l = []
        for item in input_map:
            map_l.append(item.split())

        all_lengths = []
        for item in map_l:
            all_lengths.append(len(item))

        # Convert to normalized form
        map_e = [e.lower() for item in map_l for e in item]

        # Get horizontal transpose of the map
        maph = []
        for i in range(len(map_e[0])):
            for j in range(len(map_e)):
                maph.append(["".join([map_e[j][i] ]) )

        return map_h, all_lengths

    def output(self, rm, hmmModel, labelEncoder):
        for _ in range(rm):
            mapLength = 200
            entities, states = hmmModel.sample(mapLength)
            compEntities = np.squeeze(entities)

            newState = labelEncoder.inverse_transform(compEntities)

            line_by_line = []
            for item in zip(*newState):
                line_by_line.append(''.join(item))

            # Initialize file object
            output_file_path = os.path.join('output_level', 'output_markov.txt')
            file_obj = open(output_file_path, 'w')
            index = 0
            while(index<len(line_by_line))
                file_obj.write(line_by_line[index])
                if index != len(line_by_line)-1:
                    file_obj.write('\n')
                index += 1

    def transform(self, label_encoder, final_parsed_map):
        sequence = label_encoder.transform(final_parsed_map)

        # Get map features
        map_features = np.fromiter(sequence, np.int64)
        map_features = np.atleast_2d(map_features).T

        return map_features


# main()
marioObj = MMG()

ns, rm, input_map = marioObj.getParams()
finalMap, _ = marioObj.parseInput(input_map)
bmObj = base.BaseHMM()

items = set(finalMap)
labelEncoder = bmObj.labelEncoder(items)
mapFeatures = mario_obj.transform(labelEncoder, finalMap)
hmmModel = bmObj.hmmModel(ns)
hmmModel = hmmModel.fit(mapFeatures)

marioObj.output(rm, hmmModel, labelEncoder)
